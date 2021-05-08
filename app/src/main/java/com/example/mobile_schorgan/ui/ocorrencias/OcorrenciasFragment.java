package com.example.mobile_schorgan.ui.ocorrencias;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.mobile_schorgan.DAO.OcorrenciaDAO;
import com.example.mobile_schorgan.OcorrenciaFormularioActivity;
import com.example.mobile_schorgan.R;
import com.example.mobile_schorgan.models.Ocorrencia;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OcorrenciasFragment extends Fragment {

    private ListView listOcorrencia;
    private DatePickerDialog datePickerDialog;
    private Calendar c;
    private TextView textInfo;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ocorrencias, container, false);
        listOcorrencia = (ListView) root.findViewById(R.id.list_lancamentos);
        listOcorrencia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View TextView, int position, long id) {
                Ocorrencia ocorrencia = (Ocorrencia) listOcorrencia.getItemAtPosition(position);
                Intent gotoForm = new Intent(getActivity(), OcorrenciasDetailActivity.class);
                gotoForm.putExtra("ocorrencia", ocorrencia);
                startActivity(gotoForm);
            }
        });

        listOcorrencia.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> listaminha, View item, int position, long id) {
                return false;
            }
        });

        textInfo = root.findViewById(R.id.dataInfo);
        carregaOcorrencias();
        SwitchCompat switchData = root.findViewById(R.id.switchData);
        switchData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    c = Calendar.getInstance();
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int month = c.get(Calendar.MONTH);
                    int year = c.get(Calendar.YEAR);

                    datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                            String day = String.format("%02d",mDay);
                            int monthParam = mMonth +1;
                            String month = String.format("%02d",monthParam);
                            String year = Integer.toString(mYear);
                            textInfo.setText(day+"/"+month+"/"+year);
                            c.set(mYear,mMonth,mDay);
                            carregaOcorrenciasDia(c.getTime());
                        }
                    }, day, month, year);
                    datePickerDialog.updateDate(year, month, day);
                    datePickerDialog.show();
                }
                else {
                    textInfo.setText("Todos");
                    carregaOcorrencias();
                }
            }
        });
        FloatingActionButton newItem = root.findViewById(R.id.salvar);
        newItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGoForm = new Intent(getActivity(), OcorrenciaFormularioActivity.class);
                startActivity(intentGoForm);
            }
        });


        return root;
    }

    private void carregaOcorrencias() {
        OcorrenciaDAO dao = new OcorrenciaDAO(getContext());
        List<Ocorrencia> ocorrenciaList = dao.consultaOcorrencias();
        montaAdapter(ocorrenciaList);
    }

    private void carregaOcorrenciasDia(Date date) {
        OcorrenciaDAO dao = new OcorrenciaDAO(getContext());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<Ocorrencia> ocorrenciaList = dao.consultaOcorrenciasPorData(dateFormat.format(date));
        montaAdapter(ocorrenciaList);
    }

    private void montaAdapter(List<Ocorrencia> ocorrencias) {
        OcorrenciasAdapter adapterOcorrencias = new OcorrenciasAdapter(getContext(), ocorrencias);
        listOcorrencia.setAdapter(adapterOcorrencias);

    }

    @Override
    public void onResume() {
        carregaOcorrencias();
        super.onResume();
    }
}