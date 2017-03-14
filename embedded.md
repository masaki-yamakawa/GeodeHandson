## 使用するソース、ファイルの説明

ソース/ファイル               | 説明
----------------------------- | --------------------------------------------
geode.handson.cui.P2PChat     | EmbeddedモードチャットアプリのMainクラスです。<br>ユーザー名を入力し、標準入力よりメッセージを受け取りためのコードがかかれています。<br>":q"と入力することで終了します。
/resources/gemfire.properties | 主にgeodeクラスターの設定をするためのファイルです。<br>デフォルトではクラスパス上に存在するgemfire.propertiesを探します。<br>-DgemfirePropertyeFileシステムプロパティを指定することで意図したファイルを読み込むことが可能です。
/resources/cache.xml          | キャッシュ（リージョン）を設定するためのファイルです。<br>デフォルトではクラスパス上に存在するcache.xmlを探します。<br>gemfire.propertiesのcache-xml-fileで変更することが可能です。


## キャッシュの作成

リージョンへアクセスするためには、キャッシュを作成する必要があります。
キャッシュは、すべてのデータにアクセスするためのエントリーポイントです。
キャッシュはcache.xmlのオブジェクト表現となります。
Cacheを作成するにはCacheFactoryを使用します。

今回は、P2PChat.javaの先頭に次のように追加します。

``` java
Properties props = new Properties();
if (args.length > 0 && "embeddedLocator".equals(args[0])) {
	props.setProperty("start-locator", "localhost[10335]");
} else {
	props.setProperty("locators", "localhost[10335]");
}
CacheFactory factory = new CacheFactory(props);
Cache cache = factory.create();
```

CacheFactoryのコンストラクタには、Propertiesを指定することができ、gemfire.propertiesの値を上書きすることができます。
実行毎に異なる設定値をAPIで指定することが可能です。
今回は1つ目のJavaプロセスのみ組み込み式のロケーターを起動するため、start-locatorでlocalhostの10335ポートを指定して起動しています。
2つ目以降のJavaプロセス起動では1つ目で起動したロケーターを指定して起動することでGeodeクラスターが構成されます。


## リージョンの作成

次にリージョンを作成します。
今回はcache.xmlにChatMessageという名前でREPLICATEリージョンを作成します。
ChatMessageリージョンのデータイベントを取得してメッセージを表示するため、cache-listenerでイベントハンドラーを設定しています。

``` xml
<region name="ChatMessage">
  <region-attributes refid="REPLICATE">
    <cache-listener>
      <class-name>geode.handson.cui.ChatMessageListener</class-name>
    </cache-listener>
  </region-attributes>
</region>
```

cache.xmlで作成したリージョンをJavaアプリケーションから取得するには、次のようにします。

``` java
Region<String, String> region = cache.getRegion("ChatMessage");
```


## データの登録

リージョンへデータを登録する場合は以下のように行います。

``` java
String key = String.format("%s@%s@%d", user, LocalDateTime.now().format(formatter), messageNo++);
region.put(key, message);
```

入力したユーザー名、現在日時、メッセージ番号をキーにして、全ての更新が追加登録となるようにしてデータを登録します。
（イベントハンドラでは登録のみのイベントを処理するために更新とならないようにします）

リージョンはjava.util.Map（ConcurrentMap）を継承しているため、Mapと同じ感覚でデータの操作が可能です。


## イベントハンドラ

リージョンへcache-listenerが設定されている場合は、データの追加、更新、削除、等々が行われた場合に、設定されたイベントハンドラが呼び出されます。
今回は、ChatMessageListener.javaで追加イベントのみ拾うようにして、標準出力へキーと値を出力するようにしいています。

``` java
@Override
public void afterCreate(EntryEvent<String, String> event) {
	System.out.println(event.getKey() + " > " + event.getNewValue());
}
```
