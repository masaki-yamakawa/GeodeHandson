## 概要

クライアント-サーバー型のチャットアプリ作成では、Geodeを使用してクライアント-サーバー型のアプリケーションを作成する方法を学びました。
最後は、CUIをJavaFXを使用したGUIに変更し、Geodeの設定をAPIで行う方法を学びます。


## 使用するソース、ファイルの説明

ソース/ファイル               | 説明
----------------------------- | --------------------------------------------
geode.handson.gui配下のクラス | 一般的なJavaFXアプリケーションです。<br>Geodeとの接続部分はChatClientEndpointへ集約されています。
/resources/gemfire.properties | （クライアント-サーバー型のCUIチャットアプリ作成と共有）
/resources/guiclientcache.xml | クライアントキャッシュ（リージョン）を設定するためのファイルです。<br>リージョンはAPIで作成するため空設定です。
/resources/chat.fxml          | JavaFXで使用するFXMLファイル。
/resources/chat.css           | JavaFXで使用するCSSファイル。


## ロケーター、キャッシュサーバーの起動

クライアント-サーバー型のCUIチャットアプリ作成と同様。


## リージョンの作成

クライアント-サーバー型のCUIチャットアプリ作成と同様。


## クライアントキャッシュの作成

クライアント-サーバー型のCUIチャットアプリと違いpoolの設定がないため、APIで作成する必要があります。
今回は、ChatClientEndpoint.javaのコンストラクタへ次のように追加します。

``` java
try {
	cache = ClientCacheFactory.getAnyInstance();
} catch (CacheClosedException e) {
	System.out.println("Ignore CacheClosedException");
	Properties props = new Properties();
	props.setProperty("cache-xml-file", "guiclientcache.xml");
	ClientCacheFactory factory = new ClientCacheFactory(props).addPoolLocator(host, port).setPoolSubscriptionEnabled(true);
	cache = factory.create();
}
```

ClientCacheFactory.getAnyInstance()はClientCacheがすでに存在する場合は、そのインスタンスが取得できます。
初回呼び出し（ClientCacheが作成されていない）場合には、CacheClosedExceptionが発生するため、新規に作成するようにしています。
cache.xmlの内容はXML構造にしたがってAPIで行うことが可能です。


## クライアントリージョンの作成

ここもクライアント-サーバー型のCUIチャットアプリと違いcache.xmlへ設定がないため、APIで作成する必要があります。
今回は、ChatClientEndpoint.javaのaddMessageHandlerメソッドへ次のように追加します。

``` java
Region<String, String> region = cache.getRegion(REGION_NAME);
if (region == null) {
	ClientRegionFactory<String, String> rgnFac = cache.createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY);
	region = rgnFac.addCacheListener(new ChatMessageListener()).create(REGION_NAME);
	region.registerInterest("ALL_KEYS");
}
```

リージョン属性には初期時点で設定し、それ以降変更が出来ないものと、変更が出来るものの2種類があります。
例えば、CacheListenerは任意のタイミングで追加、削除することが可能です。
変更が出来るものはregion.getAttributesMutator()を使用して変更することが可能です。


## データの登録

クライアント-サーバー型のCUIチャットアプリ作成と同様。
今回は、ChatClientEndpoint.javaのsendMessageメソッドで実行している。


## イベントハンドラ

クライアント-サーバー型のCUIチャットアプリ作成と同様。
今回は、取得したイベントをJavaFXへ渡すために今までとは別の実装をChatClientEndpoint内のChatMessageListenerで行っている。
