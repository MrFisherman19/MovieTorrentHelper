# MovieTorrentHelper
Desktop application that helps search for movie torrents. Program uses one of rapidapi's api to find movie for given phrase and then search for magnet links in piratebay site.

[Attention!] I am not responsible for any misuse or any legal consequences of using the program. I also do not encourage you to download movies from illegal sources. The program was created for an educational purpose.

# Preview

## First - user types his movie title

![First](https://i.imgur.com/wpKpHX5.png?raw=true)

## Then - he have to choose one of the movies

![Second](https://i.imgur.com/2Bfyug5.png?raw=true)

## Finally - magnet link is opened in local torrent app

![Third](https://i.imgur.com/ZC0ELZj.png?raw=true)

# What is used in project

- JavaFx
- JSoap
- One of Imdb apis from https://rapidapi.com
- JUnit5 for testing

# In which topics have I improved my knowledge

- make api calls in pure java
- matcher and pattern, regexes
- using jsoap library
- testing in junit5
- a little javafx
- a little lambdas and stream api
- a little better clean code (please look first at PirateBayMatcherFinder and then on PirateBayJSoapFinder)
