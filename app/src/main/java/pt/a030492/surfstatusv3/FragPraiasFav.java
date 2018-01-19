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
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragPraiasFav.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragPraiasFav#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragPraiasFav extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragPraiasFav.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_praias_fav, container, false);
        list = view.findViewById(R.id.list);

        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        Object o = list.getItemAtPosition(position);
//                        ecraDetalhes(FragEcraDetalhes.class, (listaPraias.get(position)[2]));//todo
                        ecraDetalhes(listaPraias.get(position)[2]);
                    }
                });

        bd = new AdaptadorBaseDados(view.getContext()).open();

        setAdap();
        if (bd.getPraiasListar().size() == 0) {
            Toast.makeText(view.getContext(), "nao tem praias adicionadas", Toast.LENGTH_LONG).show();
        }



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        bd.close();
    }

    @Override
    public void onStart(){
        super.onStart();
        bd.open();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
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
