package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;

import daryadelan.sandogh.zikey.com.daryadelan.model.ConversationTopic;
import daryadelan.sandogh.zikey.com.daryadelan.model.PersonCheck;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationTopicWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ServerWrapper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IConversationApi {

    @GET("api/Conversation/getConversation")
    Call<ConversationWrapper> getConversation();

    @POST("api/Conversation/insertConversation")
    Call<ConversationTopicWrapper> insertConversation(@Body ConversationTopic conversationTopic);


}
