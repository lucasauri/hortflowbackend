package com.hortifruti.controller;

import com.hortifruti.model.Venda;
import com.hortifruti.service.VendaService;
import com.hortifruti.service.PdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

/**
 * Controller REST para gerenciamento de vendas.
 * 
 * <p>Fornece endpoints para criar, finalizar, cancelar e consultar vendas,
 * além de gerar PDFs de recibos de venda.
 * 
 * @author Hortifruti Team
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/vendas")
@CrossOrigin(origins = "*")
@Tag(name = "Vendas", description = "API para gerenciamento de vendas")
public class VendaController {
    
    @Autowired
    private VendaService vendaService;

    @Autowired
    private PdfService pdfService;
    
    /**
     * Cria uma nova venda.
     * 
     * @param venda Dados da venda a ser criada
     * @return Venda criada ou mensagem de erro
     */
    @Operation(summary = "Criar nova venda", description = "Cria uma nova venda com status PENDENTE. Atualiza o estoque dos produtos automaticamente.")
    @PostMapping
    public ResponseEntity<?> criarVenda(@RequestBody Venda venda) {
        try {
            Venda novaVenda = vendaService.criarVenda(venda);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaVenda);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao criar venda: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarVenda(@PathVariable Long id, @RequestParam String formaPagamento) {
        try {
            Venda venda = vendaService.finalizarVenda(id, formaPagamento);
            return ResponseEntity.ok(venda);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao finalizar venda: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarVenda(@PathVariable Long id) {
        try {
            Venda venda = vendaService.cancelarVenda(id);
            return ResponseEntity.ok(venda);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao cancelar venda: " + e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Venda>> listarTodas() {
        List<Venda> vendas = vendaService.listarTodas();
        return ResponseEntity.ok(vendas);
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Venda>> listarPorCliente(@PathVariable Long clienteId) {
        List<Venda> vendas = vendaService.listarPorCliente(clienteId);
        return ResponseEntity.ok(vendas);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Venda>> listarPorStatus(@PathVariable String status) {
        try {
            Venda.StatusVenda statusVenda = Venda.StatusVenda.valueOf(status.toUpperCase());
            List<Venda> vendas = vendaService.listarPorStatus(statusVenda);
            return ResponseEntity.ok(vendas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Venda> venda = vendaService.buscarPorId(id);
        if (venda.isPresent()) {
            return ResponseEntity.ok(venda.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Venda não encontrada"));
        }
    }
    
    @GetMapping("/numero/{numeroVenda}")
    public ResponseEntity<?> buscarPorNumero(@PathVariable String numeroVenda) {
        Optional<Venda> venda = vendaService.buscarPorNumero(numeroVenda);
        if (venda.isPresent()) {
            return ResponseEntity.ok(venda.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Venda não encontrada"));
        }
    }
    
    // Classe interna para respostas de erro
    public static class ErrorResponse {
        private String message;
        
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<?> gerarPdfVenda(@PathVariable Long id) {
        try {
            Optional<Venda> venda = vendaService.buscarPorId(id);
            
            if (venda.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Venda não encontrada"));
            }
            
            ByteArrayInputStream pdfStream = pdfService.gerarPdfVenda(venda.get());
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=venda_" + venda.get().getNumeroVenda() + ".pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfStream));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao gerar PDF: " + e.getMessage()));
        }
    }
}