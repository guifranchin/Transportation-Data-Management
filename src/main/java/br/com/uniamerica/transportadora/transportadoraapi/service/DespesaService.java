package br.com.uniamerica.transportadora.transportadoraapi.service;

import br.com.uniamerica.transportadora.transportadoraapi.entity.Cidade;
import br.com.uniamerica.transportadora.transportadoraapi.entity.Despesa;
import br.com.uniamerica.transportadora.transportadoraapi.repositoty.DespesaRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DespesaService {


    @Autowired
    public DespesaRepository despesaRepository;



    public Despesa findById(Long id){
        return this.despesaRepository.findById(id).orElse(new Despesa());
    }

    public List<Despesa> findAll(){
        return this.despesaRepository.findAll();
    }


    @Transactional
    public Despesa save(Despesa despesa){
       return this.despesaRepository.save(despesa);
    }

    @Transactional
    public void atualizar(final Long id,final Despesa despesa){
        if (id.equals(despesa.getId()) && !this.despesaRepository.findById(id).isEmpty()){
            this.despesaRepository.save(despesa);
        }else{
            throw new RuntimeException("Id não encontrado");
        }
    }

    @Transactional
    public void excluir(final Long id, final Despesa despesa){
        if (id.equals(despesa.getId()) && !this.despesaRepository.findById(id).isEmpty()){
            this.despesaRepository.delete(despesa);
        }else{
            throw new RuntimeException("Id não encontrado");
        }
    }



}
