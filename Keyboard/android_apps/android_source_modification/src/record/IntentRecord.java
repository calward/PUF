package record;

import android.os.Message;
import android.os.Messenger;

/**
 * This class allows the send of Intents
 * to a service via a messenger.
 * All necessary data about the intent will be sent.
 *
 * When this class is created,
 * It will connect to the system service.
 *
 * The service will then be used to send information
 * via a messenger
 */
public class IntentRecord {
    Messenger intent_collection_service;
    boolean intent_collection_service_bound;

    /**
     * decode a message and store it as IntentData
     */
    public IntentData decode_message(Message message){

    }

    /**
     * Encode an intent as message to be submitted
     * to service
     */
    private Message encode_message(){

    }
}
