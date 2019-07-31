import MagicML.movies
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.tuning.CrossValidatorModel
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, _}
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.elasticsearch.spark.sql.EsSparkSQL

object reco  {

  def getCtx() : SparkSession = {
    SparkSession.builder.
      master("local[*]")
      .appName("spark session example")
      .getOrCreate()
  }
  def main(args: Array[String]): Unit = {
    val  sparkConf = new SparkConf().setAppName("recommander")
      .setMaster("local[*]")
      .set("spark.cores.max", "2")
      .set("spark.testing.memory", "2147480000")
      .setExecutorEnv("Churn", "Xmx1gm")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("es.nodes","localhost")
      .set("es.port","9200")
      //.set("es.nodes.wan.only", "true")
      ///.set("es.read.metadata", "true")
      .set("es.index.auto.create", "true")
      .set("es.batch.size.bytes","100mb")
      .set("es.batch.size.entries", "1000") //Elasticsearch batch size given in entries
      .set("es.batch.write.retry.wait","10s")
      .set("es.batch.write.refresh","true") //  Whether to trigger an index refresh after doing batch writing
      .set("es.batch.write.retry.count","6") //HTTP bulk retries
      .set("es.batch.write.retry.limit","50") //HTTP bulk retries
      .set("es.batch.write.retry.policy","simple")//HTTP bulk retries
      .set("es.write.operation","index")


    val scc= new SparkContext(sparkConf)
    val spark: SparkSession = SparkSession.builder().config(scc.getConf).getOrCreate()

    val ssc = new StreamingContext(scc,Seconds(4))
    val sqlContext = spark.sqlContext
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "spark-playground-group",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    val sc = getCtx()
    import sc.implicits._

    var ratings  = sc.read
      .format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load("src/main/resources/ratings.dat")
      .drop(col("timestamp"))




    var product = sc.read
      .format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load("src/main/resources/products.dat")

       product.show(5)



    val personalRatings = Seq(
      ("",0.0)
    ).toDF("title", "rating")





    // Convert ratings above into model-friendly form
    // User ID will have special value of "1"

    var userrId = 1

    var normlizedPersonalRatings = personalRatings.
      join(product, "title").
      select(lit(userrId).as("user"), col("productId").as("product") , col("rating"))

    //normlizedPersonalRatings.show(5)


    //MagicML.userId = userrId
    MagicML.movies = product
    MagicML.ratings = ratings
    MagicML.personalNormalizedRatings = normlizedPersonalRatings

    //val model = MagicML.train()

    //model.save(scc, "src/main/ressources/recommander")
    //val userrId =1
    val model=MatrixFactorizationModel.load(scc,"src/main/ressources/recommander")
    //val usersProducts = product.select(lit(userrId), col("productId")).map{
      //row => (row.getInt(0), row.getInt(1))
    //}


   // model.predict(usersProducts.rdd).toDS()

    //val result = MagicML.predict(userrId, model)
    //val df = result.filter(r => r.user == userrId)

    //val recommendationList = df.toDF().sort(col("rating").desc).join(product, product("productId") === df("product"), "inner")
    //recommendationList.select("ProductId", "title").show()
    println("name="+args(0))
    println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh")
    val inputStream = KafkaUtils.createDirectStream(ssc, PreferConsistent, Subscribe[String, String](Array(args(0)+"-customer"), kafkaParams))
inputStream.print()
    inputStream.foreachRDD(
      rowRdd=> {
        println(rowRdd)
        val rdd = rowRdd.map(rec => rec.value()).map(k => k.split("\"payload\":").reduceRight((x, y) => y)).map(k => k.substring(0, k.length() - 1))
        val df = sqlContext.read.json(rdd)
        df.show(5)
        df.printSchema()
        val ringRDD = df.rdd.map(r=>r.getLong(2).toInt)
        val rDF =ringRDD.toDF( "userId")
        println("All the Data from Kafka")
        rDF.show()
        ringRDD.cache()
        rDF.collect().foreach( x=>{
            val a =x.getInt(0)
          println("recommandation for user "+a)

          val result1 = MagicML.predict(a, model)
          val df = result1.filter(r => r.user == a)

          val recommendationList1 = df.toDF().sort(col("rating").desc).join(product, product("productId") === df("product"), "inner")
          val test=recommendationList1.select("user","ProductId", "title").limit(20)
          test.show(30)
          EsSparkSQL.saveToEs(test,"test/recommander")

        })




      }
    )
    ssc.start()
    ssc.awaitTermination()
    spark.stop()
  }
  }
