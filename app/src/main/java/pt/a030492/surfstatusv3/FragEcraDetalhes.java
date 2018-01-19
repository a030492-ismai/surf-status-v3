package pt.a030492.surfstatusv3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class FragEcraDetalhes extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView text;

    public FragEcraDetalhes() {

    }

    public static FragEcraDetalhes newInstance(String param1, String param2) {
        FragEcraDetalhes fragment = new FragEcraDetalhes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_frag_ecra_detalhes, container, false);
        text = view.findViewById(R.id.textView);

        String[] condicao = new String[2];
        condicao[0] = getArguments().getString("url");

        getConditionDescription(condicao[0]);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    @SuppressLint("StaticFieldLeak")
    private void getConditionDescription(String url) {
        new AsyncTask<String, Void, String[]>() {
            @Override
            protected void onPreExecute(){
                text.setText("a actualizar descricao...");
            }
            @Override
            protected String[] doInBackground(String... s) {
                Document fulldoc = null;

                try {
                    fulldoc = Jsoup.connect(s[0]).get();
                    s[0] = fulldoc.select("div.conditionDescription").first().text();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return s;
            }

            @Override
            protected void onPostExecute(String[] s) {
                text.setText(s[0]);
            }
        }.execute(url);
    }
}
