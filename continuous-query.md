## 概要

クライアント-サーバー型のCUIチャットアプリ作成では、リージョンのキーを指定して取得したいイベントを指定しています。
クライアントからのイベント取得ではContinuous Queryというものも利用可能です。
Continuous QueryではOQL（Object Query Language）が使用でき、OQLの結果が変わった場合にイベントが発火します。
これを使用することでリージョンのキーではなく、バリューのオブジェクトが特定の状態に変化した時に、イベントとして取得することが出来ます。

これ以降はGeodeHandsonFinalで完成した状態をもとに説明します。

## 使用するソース、ファイルの説明

ソース/ファイル                         | 説明
--------------------------------------- | --------------------------------------------
geode.handson.cui.CqChatClient          | クライアント-サーバー型でCQを使用したチャットアプリのMainクラスです。
geode.handson.cui.CqChatMessageListener | OQL結果の更新イベントをハンドルするクラスです。
/resources/cqclientcache.xml            | クライアントキャッシュ（リージョン）を設定するためのファイルです。


## jarの追加

Continuous Queryを使用する場合はgeode-coreとは別のjarが必要となります。
mavenの場合、pom.xmlに次のように依存関係を追加します。

``` xml
<dependency>
  <groupId>org.apache.geode</groupId>
  <artifactId>geode-cq</artifactId>
  <version>${geode.version}</version>
</dependency>
```


## ロケーター、キャッシュサーバーの起動、リージョン作成

ロケーター、キャッシュサーバーの起動、リージョンの作成については、実行していなければ、クライアント-サーバー型のCUIチャットアプリ作成と同様に実行してください。


## CQ用イベントハンドラ（CqChatMessageListener.java）

CQ用のイベントハンドラを実装する場合は、専用のインターフェースを実装する必要があります。
キーベースのイベントを取得する際は、データの追加、更新、削除等、データの操作毎にコールバックされるメソッドが分かれていましたが、CQの場合はonEventというメソッドのみとなります。
仮に追加なのか更新なのか削除なのかを判断したい場合はコールバックメソッドで渡されるCqEventより判断可能です。（getBaseOperation or getQueryOperation）

``` java
@Override
public void onEvent(CqEvent cqEvent) {
	System.out.println(cqEvent.getKey() + " > " + cqEvent.getNewValue());
}
```

> __【ノート】イベント種別の判断__
>
> CqEvent#getQueryOperationを使用することで、データの追加、更新、削除等のデータ操作が判断できますが、この結果はOQL結果に対するものであることに注意してください。
> 例えばConstantKey=100というKey/Valueデータがあったとして、CQで"select * from /region r where r=100"と登録していたとします。
> ConstantKey=100が200へ更新された場合、発生するのは削除イベントと追加イベントです。（更新イベントではありません）


## CQ用イベントハンドラの登録

CQ用イベントハンドラはcache.xmlで指定することが出来ません。APIで登録する必要があります。

そのためXMLでイベントハンドラの指定がないcqclientcache.xmlをAPIで指定してクライアントキャッシュを作成しています。

``` java
Properties props = new Properties();
props.setProperty("cache-xml-file", "cqclientcache.xml");
ClientCacheFactory factory = new ClientCacheFactory(props);
ClientCache cache = factory.create();
```

CQ用イベントハンドラはCqAttributesクラスに設定する必要があります。

``` java
CqAttributesFactory cqaFac = new CqAttributesFactory();
cqaFac.addCqListener(new CqChatMessageListener());
CqAttributes cqAttr = cqaFac.create();
```

作成したCqAttributesとイベント取得用のOQLを指定してCqQueryを作成します。

``` java
QueryService qService = cache.getQueryService();
CqQuery cqQuery = qService.newCq("select * from /ChatMessage", cqAttr);
```

CqQuery#executeを実行することでイベント取得が開始されます。

``` java
cqQuery.execute();
```

> __【ノート】イベント取得開始時点での初期データを取得したい場合__
>
> CqQuery#executeをCqQuery#executeWithInitialResultsへ変更することで、イベント取得開始時点での初期データを取得することが可能です。
> 初期データはexecuteWithInitialResultsの戻り値に返却されます。
