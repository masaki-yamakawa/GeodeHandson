## 概要

EmbeddedモードのCUIチャットアプリ作成では、アプリケーション中にデータの管理を行うGeodeを組み込む方法を学びました。
次は、アプリケーションとデータの管理を行うプロセスを別々にするクライアント-サーバー型の方法を学びます。


## 使用するソース、ファイルの説明

ソース/ファイル               | 説明
----------------------------- | --------------------------------------------
geode.handson.cui.ChatClient  | クライアント-サーバー型のチャットアプリのMainクラスです。<br>初期状態ではEmbeddedモードのCUIチャットアプリと同じ実装となります。
/resources/gemfire.properties | （EmbeddedモードのCUIチャットアプリ作成と共有）
/resources/clientcache.xml    | クライアントキャッシュ（リージョン）を設定するためのファイルです。<br>デフォルトではEmbeddedモードで使用したcache.xmlが使用されてしまうため、geode.propertiesのcache-xml-fileを変更するかAPIで変更する必要があります。


## ロケーター、キャッシュサーバーの起動

クライアント-サーバー型のアプリケーションを実行するには、先にGeodeのプロセスを起動する必要があります。
起動はgfsh（ジーフィッシュ）を使用しておこないます。

``` sh
{geode_install_dir}/bin/gfsh

    _________________________     __
   / _____/ ______/ ______/ /____/ /
  / /  __/ /___  /_____  / _____  /
 / /__/ / ____/  _____/ / /    / /
/______/_/      /______/_/    /_/    1.1.0

Monitor and Manage Apache Geode
gfsh>
```

gfshが起動したら以下のコマンドを使用してロケーター、キャッシュサーバーの順に起動します。
ここではMyLocator、MyCacheServer1という名前をつけて起動しています。
ログやメタデータ等はgfshを起動したディレクトリ配下にこれらの名前のディレクトリが作成され出力されます。

``` sh
gfsh>start locator --name=MyLocator
gfsh>start server --name=MyCacheServer1
```

> **【ノート】ロケーター、キャッシュサーバーの停止 **
>
> 停止する場合は以下のコマンドを実行します。
> 必ずキャッシュサーバーを停止してからロケーターを停止してください。
>
> start server --name=MyCacheServer1
> stop locator --name=MyLocator
> 
> Windows環境ではロケーターが停止しない場合があります。
> 停止しない場合はタスクマネージャーより終了させ、当該ディレクトリを一度削除して再度起動してください。


クラスターの状態を確認するにはlist membersを使用するかWebコンソールPulseを使用します。
Pulseはadmin/adminでログインできます。
左上のCluster Viewでクラスターの管理を、Data BrowserでOQLによるデータ参照が可能です。

``` sh
gfsh>list members
gfsh>start pulse
```

![image](images/pulse.jpg)


## リージョンの作成

リージョンを作成するにはgfshで以下のようにして作成します。
ちなみにgfshのヘルプを表示するにはhelp {コマンド名}とすることで表示されます。

``` sh
gfsh>create region --name=ChatMessage --type=PARTITION
```


## クライアントキャッシュの作成

クライアントキャッシュを作成する場合もキャッシュを作成する場合と同様の感覚で行うことが出来ます。
作成にはClientCacheFactoryを使用します。

今回は、ChatClient.javaの先頭に次のように追加します。

``` java
Properties props = new Properties();
props.setProperty("cache-xml-file", "clientcache.xml");
ClientCacheFactory factory = new ClientCacheFactory(props);
```

Propertiesへcache-xml-fileを指定することで、デフォルトのcache.xmlから使用する設定ファイルを変更しています。


## クライアントリージョンの作成

次にリージョンを作成します。
クライアント側にも明示的に使用するリージョンを作成する必要があります。
今回はclientcache.xmlにChatMessageという名前でCACHING_PROXYリージョンを作成します。
クライアントとそうでないものの違いは以下の点となります。

- rootタグがcacheではなく、client-cache
- 接続先をあらわすpoolが存在する。指定しているのはlocatorのホスト名とポート。イベントを取得する場合はsubscription-enabled="true"を設定する必要がある
- 使用できるリージョンタイプがサーバーとは異なる

``` xml
<pool name="ClientPool" subscription-enabled="true">
  <locator host="localhost" port="10334" />
</pool>

<region name="ChatMessage">
  <region-attributes refid="CACHING_PROXY" pool-name="ClientPool">
    <cache-listener>
      <class-name>geode.handson.cui.ChatMessageListener</class-name>
    </cache-listener>
  </region-attributes>
</region>
```

clientcache.xmlで作成したリージョンをJavaアプリケーションから取得するには、次のようにします。
ここはEmbeddedモードの場合とまったく同じです。

``` java
Region<String, String> region = cache.getRegion("ChatMessage");
```

## 取得するイベントの登録

クライアントでイベントを取得する場合にはリージョンに対して取得対象のイベントを登録する必要があります。
以下はキーワードALL_KEYSで全てのイベントを取得する場合の例です。
他にもregisterInterestを複数回実行し、特定のキーのイベントを取得したり、registerInterestRegexでキーの正規表現を使用することができます。

``` java
region.registerInterest("ALL_KEYS");
```


## データの登録

リージョンへデータを登録する場合は以下のように行います。
ここもEmbeddedモードの場合とまったく同じです。

``` java
String key = String.format("%s@%s@%d", user, LocalDateTime.now().format(formatter), messageNo++);
region.put(key, message);
```


## イベントハンドラ

イベントハンドラはEmbeddedモードと同じものが使えます。

``` java
@Override
public void afterCreate(EntryEvent<String, String> event) {
	System.out.println(event.getKey() + " > " + event.getNewValue());
}
```


## Pulseによるデータ参照

PulseではData Browserを使用してデータの参照が可能です。
Data BrowserではOQLを使用してデータ参照を行います。
OQLは非常にSQLに似ており、SQLを知っていれば感覚的に使えます。（リージョン名を指定する際に"/"を指定するぐらいです）
ただし、joinや長すぎるSQL等は制約により使えないケースがあるため、1リージョンの条件指定が出来るレベルと覚えておくと躓くことがすくなくなります。

``` sql
select * from /ChatMessage cm where cm like 'a%'
```

上記はChatMessageリージョンの値がaから始まるもの全てを参照する場合の例です。


![image](images/databrowser.jpg)
