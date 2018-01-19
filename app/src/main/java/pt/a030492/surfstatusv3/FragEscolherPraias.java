package pt.a030492.surfstatusv3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.List;

public class FragEscolherPraias extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    FloatingActionButton bActualizar;

    AdaptadorBaseDados bd;
    String urlListaPraias = "http://beachcam.meo.pt/reports/";
    ArrayList<String> arraylistPraias;
    ListView list;


    public FragEscolherPraias() {
        // Required empty public constructor
    }

    public static FragEscolherPraias newInstance(String param1, String param2) {
        FragEscolherPraias fragment = new FragEscolherPraias();
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

        View view = inflater.inflate(R.layout.fragment_frag_escolher_praias, container, false);
        list = view.findViewById(R.id.list);
        bd = new AdaptadorBaseDados(getContext()).open();

        setListAdap();
        if(bd.getPraiasListar().size() == 0){
            actualizarListaPraias();
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
        bActualizar = getActivity().findViewById(R.id.bActualizar);
        bActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarListaPraias();
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        bd.close();
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    protected void setListAdap(){
        arraylistPraias = new ArrayList<>(bd.getSize());

        for(int i = 1; i < bd.getSize() + 1; ++i){
            arraylistPraias.add(bd.getNomePraia(i));
        }

        ToggleButtonListAdapter adap = new ToggleButtonListAdapter(getContext(), arraylistPraias);
        list.setAdapter(adap);
    }

    @SuppressLint("StaticFieldLeak")
    public void actualizarListaPraias() {
//        Toast.makeText(getContext(), "a actualizar lista de praias...", Toast.LENGTH_LONG).show();
        new AsyncTask<String, Void, Document>() {

            @Override
            protected Document doInBackground(String... s) {
                Document fulldoc = null;
                try {
                    fulldoc = Jsoup.connect(s[0]).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return fulldoc;
            }

            @Override
            protected void onPostExecute(Document fulldoc) {
                actualizarListaPraiasCont(fulldoc);
            }
        }.execute(urlListaPraias);
    }

    private void actualizarListaPraiasCont(Document fulldoc) {
        ArrayList<Praia> listaPraiasTemp = new ArrayList<>();
        Elements els = fulldoc.select(".beachesContainer a");
        for(int k = 0; k < els.size() - 1; ++k){
            Praia umaPraia = new Praia(k);
            umaPraia.setNomePraia(els.get(k).text());
            umaPraia.setUrlPraia("http://beachcam.meo.pt" + els.get(k).attr("href"));
            listaPraiasTemp.add(umaPraia);
        }
        Praia.addPraias(listaPraiasTemp);
        actualizarBDPraias(listaPraiasTemp);
        setListAdap();
        Toast.makeText(getView().getContext(), "lista de praias actualizada", Toast.LENGTH_LONG).show();
    }

    private void actualizarBDPraias(List<Praia> listaPraias) {
        bd.dropPraias();
        for (Praia praia : listaPraias){
            bd.inserirPraia(praia.getNomePraia(), praia.getUrlPraia());
        }
    }

    public class ToggleButtonListAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> values;

        public ToggleButtonListAdapter(Context context, ArrayList<String> values) {
            super(context, R.layout.activity_main, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.layout_linhas, parent, false);
            TextView textView = rowView.findViewById(R.id.items);
            textView.setTextColor(Color.BLACK);
            final Switch toggleButton =  rowView.findViewById(R.id.bMostrar);
            toggleButton.setChecked(bd.getListarPraia(position+1));

            toggleButton.setOnClickListener(new View.OnClickListener() {
                private final ArrayList<String> values = arraylistPraias;

                @Override
                public void onClick(View v) {
                    if(toggleButton.isChecked()){
                        bd.updateListar(bd.getNomePraia(position+1), 1);
                    }
                    else{
                        bd.updateListar(bd.getNomePraia(position+1), 0);
                    }
                }
            });

            textView.setText(values.get(position));

            return rowView;
        }
    }
}
