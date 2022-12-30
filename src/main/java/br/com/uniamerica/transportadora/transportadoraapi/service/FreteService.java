package br.com.uniamerica.transportadora.transportadoraapi.service;

import br.com.uniamerica.transportadora.transportadoraapi.entity.Despesa;
import br.com.uniamerica.transportadora.transportadoraapi.entity.Frete;
import br.com.uniamerica.transportadora.transportadoraapi.entity.StatusFrete;
import br.com.uniamerica.transportadora.transportadoraapi.entity.Usuario;
import br.com.uniamerica.transportadora.transportadoraapi.repositoty.DespesaRepository;
import br.com.uniamerica.transportadora.transportadoraapi.repositoty.FreteRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class FreteService {

    @Autowired
    public FreteRepository freteRepository;

    @Autowired
    public HistoricoFreteService historicoFreteService;

    @Autowired
    public DespesaRepository despesaRepository;

    public Frete findById(Long id){
        return this.freteRepository.findById(id).orElse(new Frete());
    }

    public List<Frete> findByAll(){
        return this.freteRepository.findAll();
    }

    @Transactional
    public Frete save(Frete frete){
        return this.freteRepository.save(frete);
    }

    @Transactional
    public void putFrete(final Long id, final Frete frete){
        if (id.equals(frete.getId()) && !this.freteRepository.findById(id).isEmpty()){
            if (frete.getStatusFrete() == StatusFrete.CARGA){
                frete.setStatusFrete(StatusFrete.EM_TRANSPORTE);
                if(frete.getProduto() == null || frete.getMotorista() == null || frete.getCaminhao() == null ||
                frete.getPesoInicial() == null || frete.getPesoFinal() == null || frete.getPesoFinalTransportado()
                == null || frete.getPrecoTonelada() == null){
                    throw new RuntimeException("Erro no frete preencha os campos básicos");
                }else{
                    this.historicoFreteService.cadastrar(frete,StatusFrete.EM_TRANSPORTE);
                    this.freteRepository.save(frete);

                }
            }
        }
    }


    @Transactional
    public void putStatusEmTransporte(final Long id,final Frete frete){
        if (id.equals(frete.getId()) && !this.freteRepository.findById(id).isEmpty()){
            if (frete.getStatusFrete() == StatusFrete.CARGA){
                frete.setStatusFrete(StatusFrete.EM_TRANSPORTE);
                this.historicoFreteService.cadastrar(frete,StatusFrete.EM_TRANSPORTE);
                this.freteRepository.save(frete);
            }
        }else{
            throw new RuntimeException("Frete não encontrado");
        }

    }

    @Transactional
    public void putStatusInterrompido(final Long id,final Frete frete){
        if(id.equals(frete.getId()) && !this.freteRepository.findById(id).isEmpty()){
            if (frete.getStatusFrete() == StatusFrete.EM_TRANSPORTE || frete.getStatusFrete() == StatusFrete.CARGA || frete.getStatusFrete() == StatusFrete.CANCELADO){
                frete.setStatusFrete(StatusFrete.INTERROMPIDO);
                this.freteRepository.save(frete);
                this.historicoFreteService.cadastrar(frete,StatusFrete.INTERROMPIDO);
            }
        }else{
            throw new RuntimeException("Frete não encontrado");
        }
    }

    @Transactional
    public void putStatusCarga(final Long id,final Frete frete){
               frete.setStatusFrete(StatusFrete.CARGA);
                this.freteRepository.save(frete);
                this.historicoFreteService.cadastrar(frete,StatusFrete.CARGA);
    }

    @Transactional
    public void putStatusDescarga(final Long id, final Frete frete){
        if (id.equals(frete.getId()) && !this.freteRepository.findById(id).isEmpty()){
            if (frete.getStatusFrete() == StatusFrete.EM_TRANSPORTE){
                frete.setStatusFrete(StatusFrete.DESCARGA);
                this.freteRepository.save(frete);
                this.historicoFreteService.cadastrar(frete,StatusFrete.DESCARGA);
            }else{
                throw new RuntimeException("Frete não encontrado");
            }
        }
    }


    @Transactional
    public void putStatusFaturado(final Long id, final Frete frete) {
        if (id.equals(frete.getId()) && !this.freteRepository.findById(id).isEmpty()) {
            final List<Despesa> despesas = this.despesaRepository.findByAprovadorNull(frete.getId());
            Assert.isTrue(despesas.size() > 0, "Frete com despesas em aberto," +
                    "não é possivel faturalo");
            frete.setStatusFrete(StatusFrete.FATURADO);
            this.freteRepository.save(frete);
            this.historicoFreteService.cadastrar(frete,StatusFrete.FATURADO);
        }else{
            throw new RuntimeException("Frete não encontrado");
        }
    }

    @Transactional
    public void putStatusCancelado(final Long id, final Frete frete){
        if (id.equals(frete.getId()) && !this.freteRepository.findById(id).isEmpty()){
            if (frete.getStatusFrete() == StatusFrete.EM_TRANSPORTE || frete.getStatusFrete() == StatusFrete.CARGA || frete.getStatusFrete() == StatusFrete.INTERROMPIDO){
                frete.setStatusFrete(StatusFrete.CANCELADO);
                this.freteRepository.save(frete);
                this.historicoFreteService.cadastrar(frete,StatusFrete.CANCELADO);
            }
        }else{
            throw new RuntimeException("Frete não encontrado");
        }
    }

    @Transactional
    public void excluir(final Long id, final Frete frete){
        if (id.equals(frete.getId()) && !this.freteRepository.findById(id).isEmpty()){
            this.freteRepository.delete(frete);
        }else{
            throw new RuntimeException("Id não encontrado");
        }
    }

}
