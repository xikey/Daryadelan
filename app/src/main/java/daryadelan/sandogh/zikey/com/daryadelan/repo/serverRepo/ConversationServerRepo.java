package daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.ConversationTopic;
import daryadelan.sandogh.zikey.com.daryadelan.model.SendMessage;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationTopicWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.apiClient.ServerApiClient;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IConversation;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.IConversationApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationServerRepo implements IConversation {

    Call<ConversationWrapper> call;
    Call<ConversationTopicWrapper> topicCall;

    @Override
    public void getAllConversationsTopics(Context context, String tokenType, String token, int page, IRepoCallBack<ConversationWrapper> callBack) {

        IConversationApi campApi = ServerApiClient.getClientWithHeader(context, tokenType, token).create(IConversationApi.class);
        call = campApi.getConversation(page);
        call.enqueue(new Callback<ConversationWrapper>() {
            @Override
            public void onResponse(Call<ConversationWrapper> call, Response<ConversationWrapper> response) {

                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("اطلاعات جهت نمایش وجود ندارد"));
                    return;
                }

                if (response.body().getResultId() < 0) {
                    callBack.onError(new Throwable(response.body().getMessagee()));
                    return;
                }

                if (response.body().getData() == null || response.body().getData().size() == 0) {
                    callBack.onError(new Throwable("اطلاعات جهت نمایش وجود ندارد"));
                    return;
                }

                callBack.onAnswer(response.body());
            }

            @Override
            public void onFailure(Call<ConversationWrapper> call, Throwable throwable) {

                callBack.onError(new Throwable("اطلاعات جهت نمایش وجود ندارد"));
                return;
            }
        });
    }

    @Override
    public void getAllConversationsByID(Context context, String tokenType, String token, long id, IRepoCallBack<ConversationWrapper> callBack) {
        IConversationApi campApi = ServerApiClient.getClientWithHeader(context, tokenType, token).create(IConversationApi.class);
        call = campApi.getConversationById(id);
        call.enqueue(new Callback<ConversationWrapper>() {
            @Override
            public void onResponse(Call<ConversationWrapper> call, Response<ConversationWrapper> response) {

                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("اطلاعات جهت نمایش وجود ندارد"));
                    return;
                }

                if (response.body().getResultId() < 0) {
                    callBack.onError(new Throwable(response.body().getMessagee()));
                    return;
                }

                if (response.body().getData() == null || response.body().getData().size() == 0) {
                    callBack.onError(new Throwable("اطلاعات جهت نمایش وجود ندارد"));
                    return;
                }

                callBack.onAnswer(response.body());
            }

            @Override
            public void onFailure(Call<ConversationWrapper> call, Throwable throwable) {
                callBack.onError(new Throwable("خطا در دریافت اطلاعات، لطفا مجددا تلاش نمایید."));
                return;
            }
        });
    }

    @Override
    public void insertConversationTopic(Context context, String tokenType, String token, ConversationTopic conversationTopic, IRepoCallBack<ConversationTopicWrapper> callBack) {

        IConversationApi campApi = ServerApiClient.getClientWithHeader(context, tokenType, token).create(IConversationApi.class);

        topicCall = campApi.insertConversation(conversationTopic);
        topicCall.enqueue(new Callback<ConversationTopicWrapper>() {
            @Override
            public void onResponse(Call<ConversationTopicWrapper> call, Response<ConversationTopicWrapper> response) {

                if (response == null) {
                    callBack.onError(new Throwable("خطا در ثبت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("خطا در ثبت اطلاعات"));
                    return;
                }

                if (response.body().getData() == 0) {
                    callBack.onError(new Throwable("خطا در ثبت اطلاعات"));
                    return;
                }

                callBack.onAnswer(response.body());
            }

            @Override
            public void onFailure(Call<ConversationTopicWrapper> call, Throwable throwable) {
                callBack.onError(new Throwable("خطا در ثبت اطلاعات"));
            }
        });

    }

    @Override
    public void insertMessage(Context context, String tokenType, String token, String message, long conversationHeaderId, String file, IRepoCallBack<ConversationTopicWrapper> callBack) {

        IConversationApi campApi = ServerApiClient.getClientWithHeader(context, tokenType, token).create(IConversationApi.class);
        SendMessage msg = new SendMessage();
        msg.setConversationId(conversationHeaderId);
//        msg.setFilesData("0");
       msg.setMessageText(message);
        topicCall = campApi.insertMessage(msg);
        topicCall.enqueue(new Callback<ConversationTopicWrapper>() {
            @Override
            public void onResponse(Call<ConversationTopicWrapper> call, Response<ConversationTopicWrapper> response) {

                if (response == null) {
                    callBack.onError(new Throwable("خطا در ثبت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("خطا در ثبت اطلاعات"));
                    return;
                }


                callBack.onAnswer(response.body());
            }

            @Override
            public void onFailure(Call<ConversationTopicWrapper> call, Throwable t) {
                callBack.onError(new Throwable("خطا در ثبت اطلاعات"));
            }
        });

    }
}
