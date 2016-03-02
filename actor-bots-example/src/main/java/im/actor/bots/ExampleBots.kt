package im.actor.bots

import im.actor.bots.framework.MagicBotFork
import im.actor.bots.framework.MagicBotMessage
import im.actor.bots.framework.MagicBotTextMessage
import im.actor.bots.framework.MagicForkScope
import im.actor.bots.framework.persistence.MagicPersistentBot
import im.actor.bots.framework.stateful.MagicStatefulBot
import im.actor.bots.framework.stateful.isText
import im.actor.bots.framework.stateful.oneShot
import im.actor.bots.framework.stateful.text
import org.json.JSONObject

/**
 * Very simple echo bot that forwards message
 */
class EchoBot(scope: MagicForkScope) : MagicBotFork(scope) {

    override fun onMessage(message: MagicBotMessage) {
        when (message) {
            is MagicBotTextMessage -> {
                sendText("Received: ${message.text}")
            }
        }
    }
}

class EchoStatefulBot(scope: MagicForkScope) : MagicStatefulBot(scope) {
    override fun configure() {
        // Configure group behaviour
        ownNickname = "echo"
        enableInGroups = true
        onlyWithMentions = false

        oneShot("/start") {
            sendText("Hi, i'm simple echo bot, send me text and i'll send it back.")
        }

        oneShot("default") {
            if (isText) {
                sendText(text)
            }
        }

    }

}

/**
 * Echo persistent bot that keeps it's state between restart
 */
class EchoPersistentBot(scope: MagicForkScope) : MagicPersistentBot(scope) {

    var receivedCount: Int = 0

    override fun onRestoreState(state: JSONObject) {
        receivedCount = state.optInt("counter", 0)
    }

    override fun onMessage(message: MagicBotMessage) {
        sendText("Received ${receivedCount++} messages")
    }

    override fun onSaveState(state: JSONObject) {
        state.put("counter", receivedCount)
    }
}