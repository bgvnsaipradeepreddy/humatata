package com.hakunamatata.pradeep.noworries;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by srinu on 1/1/2016.
 */
public class FragmentGroupChat extends Fragment {


    String location;
    int userId,placeId;
    View view;

    private Button btnSend;
    private EditText inputMsg;


    // Chat messages list adapter
    private MessagesListAdapter adapter;
    private List<GroupChatMessages> listMessages;
    private ListView listViewMessages;

    private Utils utils;

    // Client name
    private String name = null;

    // JSON flags to identify the kind of JSON response
    private static final String TAG_SELF = "self", TAG_NEW = "new",
            TAG_MESSAGE = "message", TAG_EXIT = "exit";

    public static FragmentGroupChat newInstance(int userId, int placeId,String location) {

        FragmentGroupChat fragment = new FragmentGroupChat();
        Bundle args = new Bundle();
        args.putString("place", location);
        args.putInt("place_id", placeId);
        args.putInt("user_id", userId);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentGroupChat() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        location = getArguments().getString("place");
        userId = getArguments().getInt("user_id");
        placeId = getArguments().getInt("place_id");
        view = inflater.inflate(R.layout.chat_group_fragment, container, false);

        btnSend = (Button) view.findViewById(R.id.bFragmentGroupChat);
        inputMsg = (EditText) view.findViewById(R.id.etFragmentGroupChat);
        listViewMessages = (ListView) view.findViewById(R.id.lvFragmentGroupChat);

        //utils = new Utils(getContext());

        // Getting the person name from previous screen
        //Intent i = getContext().getIntent();
        //name = i.getStringExtra("name");

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Sending message to web socket server
                //sendMessageToServer(utils.getSendMessageJSON(inputMsg.getText()
                //        .toString()));
                String input = inputMsg.getText().toString();
                Log.e("chatpradeep",input);
                GroupChatMessages m1 = new GroupChatMessages("pradeep", inputMsg.getText().toString(), true);
                GroupChatMessages m2 = new GroupChatMessages("pradeep", inputMsg.getText().toString(), false);
                appendMessage(m1);
                appendMessage(m2);

                // Clearing the input filed once message was sent
                inputMsg.setText("");
            }
        });

        listMessages = new ArrayList<GroupChatMessages>();

        adapter = new MessagesListAdapter(getActivity(), listMessages);
        listViewMessages.setAdapter(adapter);

        return view;
    }

    private void appendMessage(final GroupChatMessages m) {

                listMessages.add(m);

                adapter.notifyDataSetChanged();


    }


    class DealsTask extends AsyncTask<RequestPackage, String, String> {

        JsonParser jsonParser = new JsonParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("pradeepdebug", "querypre");

        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            String jsonOutput;
            Log.e("inplaces6", "inplaces6");
            HttpManager httpManager = new HttpManager();
            jsonOutput = httpManager.getData(params[0]);
            Log.e("inplaces7","inplaces7");
            Log.e("data", jsonOutput);
            return jsonOutput;
            //

        }


    }



    class MessagesListAdapter extends BaseAdapter {

        private Context context;
        private List<GroupChatMessages> messagesItems;

        public MessagesListAdapter(Context context, List<GroupChatMessages> navDrawerItems) {
            this.context = context;
            this.messagesItems = navDrawerItems;
        }

        @Override
        public int getCount() {
            return messagesItems.size();
        }

        @Override
        public Object getItem(int position) {
            return messagesItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            /**
             * The following list not implemented reusable list items as list items
             * are showing incorrect data Add the solution if you have one
             * */

            GroupChatMessages m = messagesItems.get(position);

            LayoutInflater mInflater = (LayoutInflater) getContext()
                    .getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);

            // Identifying the message owner
            if (messagesItems.get(position).isSelf()) {
                // message belongs to you, so load the right aligned layout
                convertView = mInflater.inflate(R.layout.list_item_message_right,
                        null);
            } else {
                // message belongs to other person, load the left aligned layout
                convertView = mInflater.inflate(R.layout.list_item_message_left,
                        null);
            }

            TextView lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
            TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);

            txtMsg.setText(m.getMessage());
            lblFrom.setText(m.getFromName());

            return convertView;
        }
    }

}