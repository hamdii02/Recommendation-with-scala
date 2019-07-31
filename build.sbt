name := "RecommendationSystem"

version := "0.1"

scalaVersion := "2.11.4"

//libraryDependencies ++= {
  val sparkVer = "2.1.0"
  //Seq(
libraryDependencies +="org.apache.spark" %% "spark-core" % sparkVer % "provided" withSources()
libraryDependencies +="org.apache.spark" %% "spark-sql" % sparkVer
//libraryDependencies +="org.apache.spark" %% "spark-mllib" % "2.1.0"
//https://mvnrepository.com/artifact/org.apache.hadoop/hadoop-hdfs -->
//libraryDependencies +=org="org.apache.hadoop" name="hadoop-hdfs" rev="3.2.0"

// https://mvnrepository.com/artifact/org.apache.hadoop/hadoop-hdfs
//libraryDependencies += "org.apache.hadoop" % "hadoop-hdfs" % "3.1.0"


//libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

// https://mvnrepository.com/artifact/org.scalactic/scalactic
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.0-SNAP10"

// https://mvnrepository.com/artifact/org.scalatest/scalatest
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test

// https://mvnrepository.com/artifact/com.typesafe/config
libraryDependencies += "com.typesafe" % "config" % "1.2.1"


// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.9.7"
// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.7"

// https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-scala
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.7"

fork in run := true

// https://mvnrepository.com/artifact/org.apache.spark/spark-core
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.0"

// https://mvnrepository.com/artifact/org.apache.commons/commons-csv
libraryDependencies += "org.apache.commons" % "commons-csv" % "1.6"


// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
//libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.0"


// https://mvnrepository.com/artifact/org.apache.spark/spark-yarn
libraryDependencies += "org.apache.spark" %% "spark-yarn" % "2.4.0"




libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.4.0"



//Elasticsearch 


// temporary hack until we no longer need the customized elasticsearch-hadoop-mr
resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"


libraryDependencies += "org.elasticsearch" % "elasticsearch" % "7.1.1"

// https://mvnrepository.com/artifact/org.nlpcn/elasticsearch-sql
libraryDependencies += "org.nlpcn" % "elasticsearch-sql" % "6.3.0.0"


// https://mvnrepository.com/artifact/org.elasticsearch/elasticsearch-spark-20
libraryDependencies += "org.elasticsearch" %% "elasticsearch-spark-20" % "7.1.1"


// https://mvnrepository.com/artifact/org.elasticsearch/elasticsearch-x-content
libraryDependencies += "org.elasticsearch" % "elasticsearch-x-content" % "7.1.1"

// https://mvnrepository.com/artifact/commons-httpclient/commons-httpclient
libraryDependencies += "commons-httpclient" % "commons-httpclient" % "3.1"


//https://mvnrepository.com/artifact/org.elasticsearch/elasticsearch-hadoop
//libraryDependencies += "org.elasticsearch" % "elasticsearch-hadoop" % "6.7.0"

// https://mvnrepository.com/artifact/com.wizzardo.tools/tools-json
libraryDependencies += "com.wizzardo.tools" % "tools-json" % "0.22"

enablePlugins(JavaAppPackaging)

  //)
//}
