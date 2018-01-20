package pt.a030492.surfstatusv3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class FragPraiasFav extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ListView list;
    AdaptadorBaseDados bd;
    ArrayAdapter adap;
    ArrayList<String[]> listaPraias;

    FloatingActionButton bActualizar;

    public FragPraiasFav() {
        // Required empty public constructor
    }

    public static FragPraiasFav newInstance(String param1, String param2) {
        FragPraiasFav fragment = new FragPraiasFav();
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

        View view = inflater.inflate(R.layout.fragment_frag_praias_fav, container, false);
        list = view.findViewById(R.id.list);
        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        Object o = list.getItemAtPosition(position);

                        ecraDetalhes(listaPraias.get(position)[2]);
                    }
                });

        bd = new AdaptadorBaseDados(view.getContext()).open();

        bActualizar = getActivity().findViewById(R.id.bActualizar);

        bActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bd.getPraiasListar().size() == 0) {
                    Toast.makeText(getContext(), "nao tem praias adicionadas", Toast.LENGTH_LONG).show();
                }
                updateCondicoes();
            }
        });

        setAdap();
        if (bd.getPraiasListar().size() == 0) {
            Toast.makeText(view.getContext(), "nao tem praias adicionadas", Toast.LENGTH_LONG).show();
        }

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
        bd.close();
    }

    @Override
    public void onPause(){
        super.onPause();
        bd.close();
    }

    @Override
    public void onResume(){
        super.onResume();
        bd.open();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        bd.close();
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

        @SuppressLint("StaticFieldLeak")
        private void updateCondicoes () {

        for (final String[] praia : listaPraias) {
            new AsyncTask<String, Long, String[]>() {

                @Override
                protected void onPreExecute() {
                    praia[1] = "a carregar...";
                    list.setAdapter(adap);
                }

                @Override
                protected String[] doInBackground(String... s) {
                    Document doc;
                    try {
                        doc = Jsoup.connect(s[2]).get();
                        s[1] = doc.select("div.classificationDescription").first().text();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return s;
                }

                @Override
                protected void onPostExecute(String[] s) {
                    bd.updateCondicaoPraia(s);
                    list.setAdapter(adap);
                }
            }.execute(praia);
        }
    }

    private void setAdap() {
        listaPraias = new ArrayList<>(bd.getSize());
        listaPraias = bd.getPraiasListar();

        adap = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, listaPraias) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                text1.setText(listaPraias.get(position)[0]);
                text2.setText(listaPraias.get(position)[1]);
                return view;
            }
        };

        list.setAdapter(adap);
    }

    private void ecraDetalhes(String s) {
        Fragment fragment = null;
        Bundle args = new Bundle();
        args.putString("url", s);

        fragment = new FragEcraDetalhes();
        fragment.setArguments(args);
        if (fragment != null) {

            android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
