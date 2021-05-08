package com.example.mobile_schorgan.ui.clientes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobile_schorgan.DAO.ClienteDAO;
import com.example.mobile_schorgan.ClienteFormularioActivity;
import com.example.mobile_schorgan.R;
import com.example.mobile_schorgan.models.Cliente;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ClientesFragment extends Fragment {

    private ListView listaminha;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_clientes, container, false);
        listaminha = (ListView) root.findViewById(R.id.my_list);

        listaminha.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View TextView, int position, long id) {
                Cliente cliente = (Cliente) listaminha.getItemAtPosition(position);
                Intent gotoForm = new Intent(getActivity(), ClienteFormularioActivity.class);
                gotoForm.putExtra("cliente", cliente);
                startActivity(gotoForm);
            }
        });

        listaminha.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> listaminha, View item, int position, long id) {
                return false;
            }
        });

        carregaPessoas();
        FloatingActionButton newItem = root.findViewById(R.id.main_salvar);
        newItem.setOnClickListener(new View.OnClickListener() {
            // abaixo segue uma referencia para ele navegar entre as telas
            @Override
            public void onClick(View v) {
                Intent intentGoForm = new Intent(getActivity(), ClienteFormularioActivity.class);
                startActivity(intentGoForm);
            }
        });
        registerForContextMenu(listaminha);
        return root;
    }

    private void carregaPessoas() {
        ClienteDAO dao = new ClienteDAO(getContext());
        List<Cliente> clientes = dao.buscaClientes();
        dao.close();

        ClienteAdapter clienteAdapter = new ClienteAdapter(getContext(), clientes);
        //ArrayAdapter<Cliente> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, clientes);
        listaminha.setAdapter(clienteAdapter);
    }

    @Override
    public void onResume() {
        carregaPessoas();
        super.onResume();
    }
}