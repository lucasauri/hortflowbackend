package com.hortifruti.controller;

import com.hortifruti.model.Venda;
import com.hortifruti.service.VendaService;
import com.hortifruti.service.PdfService;
import com.hortifruti.repository.VendaRepository;
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

    @Autowired
    private VendaRepository vendaRepository;
    
    /**
     * Construtor padrão.
     */
    public VendaController() {
    }

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
    
    /**
     * Finaliza uma venda.
     * @param id O ID da venda.
     * @param formaPagamento A forma de pagamento.
     * @return A venda finalizada.
     */
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarVenda(@PathVariable Long id, @RequestParam(required = false) String formaPagamento) {
        try {
            if (formaPagamento == null || formaPagamento.isBlank()) {
                formaPagamento = "pix"; // valor padrão para robustez quando front não enviar
            }
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

    /**
     * Finaliza uma venda por número.
     * @param numero O número da venda.
     * @param formaPagamento A forma de pagamento.
     * @return A venda finalizada.
     */
    @PutMapping("/numero/{numero}/finalizar")
    public ResponseEntity<?> finalizarVendaPorNumero(@PathVariable String numero, @RequestParam(required = false) String formaPagamento) {
        try {
            if (formaPagamento == null || formaPagamento.isBlank()) {
                formaPagamento = "pix";
            }
            Venda venda = vendaService.finalizarVendaPorNumero(numero, formaPagamento);
            return ResponseEntity.ok(venda);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao finalizar venda: " + e.getMessage()));
        }
    }

    /**
     * Finaliza a venda e retorna o PDF do recibo.
     * @param id O ID da venda.
     * @param formaPagamento A forma de pagamento.
     * @return O PDF do recibo.
     */
    @Operation(summary = "Finalizar venda e gerar PDF", description = "Finaliza a venda e retorna o PDF do recibo em linha.")
    @PutMapping("/{id}/finalizar/pdf")
    public ResponseEntity<?> finalizarVendaGerarPdf(@PathVariable Long id, @RequestParam(required = false) String formaPagamento) {
        try {
            if (formaPagamento == null || formaPagamento.isBlank()) {
                formaPagamento = "pix"; // valor padrão
            }
            Venda vendaFinalizada = vendaService.finalizarVenda(id, formaPagamento);
            // Recarrega com itens/produto/cliente para evitar LazyInitialization ao gerar PDF
            Venda venda = vendaRepository.findByIdWithItensProdutoCliente(vendaFinalizada.getId())
                    .orElseThrow(() -> new RuntimeException("Venda não encontrada para gerar PDF"));

            ByteArrayInputStream pdfStream = pdfService.gerarPdfVenda(venda);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=venda_" + venda.getNumeroVenda() + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfStream));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao finalizar venda e gerar PDF: " + e.getMessage()));
        }
    }
    
    /**
     * Cancela uma venda.
     * @param id O ID da venda.
     * @return A venda cancelada.
     */
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
    
    /**
     * Lista todas as vendas.
     * @return Uma lista de todas as vendas.
     */
    @GetMapping
    public ResponseEntity<List<Venda>> listarTodas() {
        List<Venda> vendas = vendaService.listarTodas();
        return ResponseEntity.ok(vendas);
    }
    
    /**
     * Lista as vendas de um cliente.
     * @param clienteId O ID do cliente.
     * @return Uma lista de vendas do cliente.
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Venda>> listarPorCliente(@PathVariable Long clienteId) {
        List<Venda> vendas = vendaService.listarPorCliente(clienteId);
        return ResponseEntity.ok(vendas);
    }
    
    /**
     * Lista as vendas por status.
     * @param status O status da venda.
     * @return Uma lista de vendas com o status especificado.
     */
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
    
    /**
     * Busca uma venda por ID.
     * @param id O ID da venda.
     * @return A venda, se encontrada.
     */
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
    
    /**
     * Busca uma venda pelo número.
     * @param numeroVenda O número da venda.
     * @return A venda, se encontrada.
     */
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
    
    /**
     * Classe interna para respostas de erro.
     */
    public static class ErrorResponse {
        private String message;
        
        /**
         * Construtor da classe de resposta de erro.
         * @param message A mensagem de erro.
         */
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        /**
         * Retorna a mensagem de erro.
         * @return A mensagem de erro.
         */
        public String getMessage() {
            return message;
        }
        
        /**
         * Define a mensagem de erro.
         * @param message A mensagem de erro.
         */
        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * Gera o PDF de uma venda.
     * @param id O ID da venda.
     * @return O PDF da venda.
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<?> gerarPdfVenda(@PathVariable Long id) {
        try {
            Optional<Venda> venda = vendaRepository.findByIdWithItensProdutoCliente(id);
            
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