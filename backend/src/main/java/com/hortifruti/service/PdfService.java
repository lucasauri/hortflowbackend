package com.hortifruti.service;

import com.hortifruti.model.ItemVenda;
import com.hortifruti.model.Venda;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.HorizontalAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {
    
    public ByteArrayInputStream gerarPdfVenda(Venda venda) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            // Título
            document.add(new Paragraph("HORTIFLOW")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            
            document.add(new Paragraph("Sistema de Gestão de Hortifruti")
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER));
            
            document.add(new Paragraph("\n"));
            
            // Cabeçalho da Venda
            document.add(new Paragraph("RECIBO DE VENDA")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            
            document.add(new Paragraph("\n"));
            
            // Informações da Venda
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}));
            infoTable.setWidth(UnitValue.createPercentValue(100));
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            
            infoTable.addCell(createCell("Número da Venda:").setBold());
            infoTable.addCell(createCell(venda.getNumeroVenda()));
            
            infoTable.addCell(createCell("Data da Venda:").setBold());
            infoTable.addCell(createCell(venda.getDataVenda().format(formatter)));
            
            infoTable.addCell(createCell("Cliente:").setBold());
            infoTable.addCell(createCell(venda.getCliente().getNome()));
            
            infoTable.addCell(createCell("CPF/CNPJ:").setBold());
            String cpfCnpj = venda.getCliente().getCpf();
            if (venda.getCliente().getCnpj() != null && !venda.getCliente().getCnpj().isEmpty()) {
                cpfCnpj += " / " + venda.getCliente().getCnpj();
            }
            infoTable.addCell(createCell(cpfCnpj));
            
            infoTable.addCell(createCell("Telefone:").setBold());
            infoTable.addCell(createCell(venda.getCliente().getTelefone()));
            
            infoTable.addCell(createCell("Status:").setBold());
            infoTable.addCell(createCell(venda.getStatus().toString()));
            
            infoTable.addCell(createCell("Forma de Pagamento:").setBold());
            infoTable.addCell(createCell(venda.getFormaPagamento() != null ? venda.getFormaPagamento() : "Não especificada"));
            
            document.add(infoTable);
            document.add(new Paragraph("\n"));
            
            // Itens da Venda
            document.add(new Paragraph("ITENS DA VENDA")
                    .setFontSize(14)
                    .setBold());
            
            Table itensTable = new Table(UnitValue.createPercentArray(new float[]{3, 1, 2, 2}));
            itensTable.setWidth(UnitValue.createPercentValue(100));
            
            // Cabeçalho
            itensTable.addHeaderCell(createHeaderCell("Produto"));
            itensTable.addHeaderCell(createHeaderCell("Qtd"));
            itensTable.addHeaderCell(createHeaderCell("Preço Unit."));
            itensTable.addHeaderCell(createHeaderCell("Subtotal"));
            
            // Itens
            for (ItemVenda item : venda.getItens()) {
                itensTable.addCell(createCell(item.getProduto().getNome()));
                itensTable.addCell(createCell(item.getQuantidade().toString()));
                itensTable.addCell(createCell("R$ " + String.format("%.2f", item.getPrecoUnitario())));
                itensTable.addCell(createCell("R$ " + String.format("%.2f", item.getSubtotal())));
            }
            
            document.add(itensTable);
            document.add(new Paragraph("\n"));
            
            // Totais
            Table totaisTable = new Table(UnitValue.createPercentArray(new float[]{2, 2}));
            totaisTable.setWidth(UnitValue.createPercentValue(50));
            totaisTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            
            totaisTable.addCell(createCell("Valor Total:").setBold());
            totaisTable.addCell(createCell("R$ " + String.format("%.2f", venda.getValorTotal())));
            
            if (venda.getDesconto().compareTo(BigDecimal.ZERO) > 0) {
                totaisTable.addCell(createCell("Desconto:").setBold());
                totaisTable.addCell(createCell("R$ " + String.format("%.2f", venda.getDesconto())));
            }
            
            totaisTable.addCell(createCell("Valor Final:").setBold());
            totaisTable.addCell(createCell("R$ " + String.format("%.2f", venda.getValorFinal())));
            
            document.add(totaisTable);
            
            // Observações
            if (venda.getObservacoes() != null && !venda.getObservacoes().isEmpty()) {
                document.add(new Paragraph("\n"));
                document.add(new Paragraph("Observações:")
                        .setBold());
                document.add(new Paragraph(venda.getObservacoes()));
            }
            
            // Rodapé
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("______________________________________________")
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Assinatura do Cliente")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(10));
            
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Obrigado pela preferência! Volte sempre!")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12));
            
            document.close();
            
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF da venda", e);
        }
    }
    
    private Cell createCell(String content) {
        return new Cell().add(new Paragraph(content))
                .setPadding(5);
    }
    
    private Cell createHeaderCell(String content) {
        return new Cell().add(new Paragraph(content))
                .setBold()
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setPadding(5);
    }
}