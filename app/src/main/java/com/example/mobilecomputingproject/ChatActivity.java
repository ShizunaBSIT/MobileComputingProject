package com.example.mobilecomputingproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.CountTokensResponse;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.GenerationConfig;
import com.google.ai.client.generativeai.type.RequestOptions;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class ChatActivity extends AppCompatActivity {

    private EditText messageEditText;
    private Button sendButton, backButton;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messageList = new ArrayList<>();
    private GenerativeModelFutures generativeModel; // For Java
    private Executor mainExecutor;

    private Content systemChatbotInstruction;
    private static final String api_key = "AIzaSyCnjeKfahacg_DlirfP-kIoxfTLEpqXaoM";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.chatbot_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chatbot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        messageEditText = findViewById(R.id.msgInput);
        sendButton = findViewById(R.id.sendBtn);
        backButton = findViewById(R.id.backBtn);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);

        mainExecutor = ContextCompat.getMainExecutor(this);

        ///  AIIIIIIIIIIIIIIIIIIIIIIIIIIIII AIIIIIIIIIIIIIIIIIII
        String chatbotInstruction = "You are a helpful assistant app on an app named Wandermart. " +
                "Wandermart is an app made for users who are looking to buy foreign goods in the Philippines as well as provide a platform for" +
                "small business owners to sell their products on. The small businesses in question are usually grocery stores or goods stores that sell specifically goods imported from other nations focusing on food and food ingredients" +
                "Here is a list of the FAQs and the general answers to them:" +
                "1. How do I login into the app? Answer: Please navigate to the profile tab, indicated by the profile icon on the bottom navigation bar. Then fill out the login form" +
                "2. How do I buy something? Answer: Browse through the home menu and if there is any product you'd like to buy, you can click on its respective buy button." +
                "3. The autofill address button doesn't work. Answer: You may not have given this app any location permissions, in that case, please modify your app permissions or manually input your address" +
                "There will be other questions regarding the app, especially about WanderMart itself, the best way to explain this app is that it is an ecommerce platform for foreign foods and food ingredients.";

        systemChatbotInstruction = new Content.Builder().addText(chatbotInstruction).build();
        RequestOptions requestOptions = new RequestOptions(Long.parseLong("5000"));

        GenerativeModel coreModel = new GenerativeModel("gemini-2.0-flash", api_key,
                null,
                null,
                requestOptions,
                null,
                null,
                systemChatbotInstruction);
        generativeModel = GenerativeModelFutures.from(coreModel);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput = String.valueOf(messageEditText.getText());
                if (!userInput.isEmpty()) {
                    addMessageToUi(userInput, true);
                    messageEditText.setText("");

                    if (generativeModel != null) {
                        getBotResponse(userInput);
                    }
                    else {
                        addMessageToUi("Error: Chatbot cannot be initialized", false);
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (generativeModel != null) {
            addMessageToUi("Hello! How can I help you today?", false);
        }
        else if (api_key == null) {
            addMessageToUi("Error: API key is missing", false);
        }

    }

    private void addMessageToUi(String text, boolean isUser) {
        Message message = new Message(text, isUser, System.currentTimeMillis());
        messageList.add(message);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.scrollToPosition(messageList.size() - 1);
    }

    private void getBotResponse(String userInput) {
        String thinkingMessagetext = "Bot is thinking";

        addMessageToUi(thinkingMessagetext, false);
        int thinkingMessagePosition = messageList.size() - 1;

        Content content = new Content.Builder().addText(userInput).build();

        ListenableFuture<GenerateContentResponse> future = generativeModel.generateContent(content);
        Futures.addCallback(future, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                if (thinkingMessagePosition < messageList.size() &&
                        messageList.get(thinkingMessagePosition).getText().equals(thinkingMessagetext) ){
                    messageList.remove(thinkingMessagePosition);
                    chatAdapter.notifyItemRemoved(thinkingMessagePosition);
                }

                String botResponse;

                if (result.getText() != null ) {
                    botResponse = result.getText();
                }
                else {
                    botResponse = "Sorry, I didn't get that. Please try again";
                    Log.e("ChatActivity", "Gemini API response text is null. Full response: "+result);
                }

                addMessageToUi(botResponse,false);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("ChatActivity", "Error getting content from Gemini API", t);
                if (thinkingMessagePosition < messageList.size() &&
                        messageList.get(thinkingMessagePosition).getText().equals(thinkingMessagetext) ){
                    messageList.remove(thinkingMessagePosition);
                    chatAdapter.notifyItemRemoved(thinkingMessagePosition);
                }

                addMessageToUi("Error: "+t.getMessage(), false);
            }


        }, mainExecutor);
    }

}
