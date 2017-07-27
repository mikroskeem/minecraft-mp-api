# Votereaper

Minecraft-mp.com API wrapper

## Dependencies
Kotlin, OkHttp & Gson

## Example usage
```kotlin
import com.google.gson.Gson
import eu.mikroskeem.votereaper.MinecraftMPServer
import okhttp3.OkHttpClient

object Main {
    @JvmStatic
    fun main(args: Array<out String>) {
        // Server API key. Register your server at http://minecraft-mp.com and grab API key for your server
        val key = (if(args.isNotEmpty()) args[0] else null) ?: return

        // Construct MinecraftMPServer object. 
        val server = MinecraftMPServer(key)
        // Optionally pass custom okhttp/gson instance: MinecraftMPServer(key, OkHttpClient(), Gson())

        // http://minecraft-mp.com/help/api/#api_option1_content
        println(server.getVoteStatus("foobar"))

        // http://minecraft-mp.com/help/api/#api_option2_content
        println(server.setVoteClaimed("foobar"))

        // http://minecraft-mp.com/help/api/#api_option4_content
        println(server.getListOfVotes())

        // http://minecraft-mp.com/help/api/#api_option3_content
        println(server.getListOfVoters(MinecraftMPServer.VotePeriods.CURRENT_MONTH))

        // http://minecraft-mp.com/help/api/#api_option5_content
        println(server.getFullServerInfo())
    }
}
```