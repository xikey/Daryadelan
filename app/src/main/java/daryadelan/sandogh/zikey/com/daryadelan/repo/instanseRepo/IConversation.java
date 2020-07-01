package daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.ConversationTopic;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationTopicWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ServerWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;

public interface IConversation {

    void getAllConversationsTopics(Context context, String tokenType, String token,int page, IRepoCallBack<ConversationWrapper> callBack);

    void getAllConversationsByID(Context context, String tokenType, String token,long id, IRepoCallBack<ConversationWrapper> callBack);

    void insertConversationTopic(Context context, String tokenType, String token, ConversationTopic conversationTopic,  IRepoCallBack<ConversationTopicWrapper> callBack);


}
