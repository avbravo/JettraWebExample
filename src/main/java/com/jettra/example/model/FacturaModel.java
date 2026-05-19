package com.jettra.example.model;

import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.PropertiesLabel;
import io.jettra.wui.core.annotations.ViewDataTable;
import io.jettra.wui.core.annotations.ViewSelectOne;
import io.jettra.wui.validations.NotNull;
import java.time.LocalDate;
import java.util.List;

@JettraViewModel
public class FacturaModel {
    @NotNull
    @PropertiesLabel(value = "factura.id", label = "ID Factura")
    private Long idFactura;


    @NotNull
    @ViewSelectOne(label = "nombre", source = "ClienteRepository", method = "findAll")
    @PropertiesLabel(value = "factura.cliente", label = "Cliente")
    private ClienteModel clienteModel;
    
    
    @NotNull
    @PropertiesLabel(value = "factura.fechaEmision", label = "Fecha de Emisión")
    private LocalDate fechaEmision;

    @ViewDataTable(editableRowMaster = false,showRowInMasterTable = false,row="productoId, precio, cantidad, total", editablerow="productoId, cantidad", source="FacturaRepository", method="getLineas")
    @PropertiesLabel(value = "factura.lineas", label = "Detalle de Líneas")
    private List<LineaFacturaModel> lineaFacturaModel;

    @PropertiesLabel(value = "factura.porcentajeImpuesto", label = "Porcentaje Impuesto (%)")
    private Double porcentajeImpuesto;

    @PropertiesLabel(value = "factura.subtotal", label = "Subtotal")
    private Double subtotal;

    @PropertiesLabel(value = "factura.descuento", label = "Descuento")
    private Double descuento;

    @PropertiesLabel(value = "factura.total", label = "Total")
    private Double total;

    public FacturaModel() {}

    public FacturaModel(Long idFactura, ClienteModel clienteModel, LocalDate fechaEmision, List<LineaFacturaModel> lineaFacturaModel, Double porcentajeImpuesto, Double subtotal, Double descuento, Double total) {
        this.idFactura = idFactura;
        this.clienteModel = clienteModel;
        this.fechaEmision = fechaEmision;
        this.lineaFacturaModel = lineaFacturaModel;
        this.porcentajeImpuesto = porcentajeImpuesto;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
    }

    public Long getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    public ClienteModel getClienteModel() {
        return clienteModel;
    }

    public void setClienteModel(ClienteModel clienteModel) {
        this.clienteModel = clienteModel;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public List<LineaFacturaModel> getLineaFacturaModel() {
        return lineaFacturaModel;
    }

    public void setLineaFacturaModel(List<LineaFacturaModel> lineaFacturaModel) {
        this.lineaFacturaModel = lineaFacturaModel;
    }

    public Double getPorcentajeImpuesto() {
        return porcentajeImpuesto;
    }

    public void setPorcentajeImpuesto(Double porcentajeImpuesto) {
        this.porcentajeImpuesto = porcentajeImpuesto;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    

    
    
}
