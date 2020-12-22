package org.dis.front;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.dis.back.BRException;
import org.dis.back.EmpleadoBR;

import java.util.ArrayList;
import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        final VerticalLayout mainLayout = new VerticalLayout();

        TabSheet tabsheet = new TabSheet();

        // Estructura Tab salario bruto

        final VerticalLayout salarioBrutoContainer = new VerticalLayout();
        final HorizontalLayout salarioBruto = new HorizontalLayout();

        // Fin estructura Tab salario bruto

        // Estructura Tab salario neto

        final VerticalLayout salarioNetoContainer = new VerticalLayout();
        final HorizontalLayout salarioNeto = new HorizontalLayout();

        // Fin estructura Tab salario neto

        // Result layouts

        final VerticalLayout resSalarioBrutoLayout = new VerticalLayout();
        final VerticalLayout resSalarioNetoLayout = new VerticalLayout();

        // Fin result layouts

        // Parámetros salario bruto
//        final TextField tipoEmpleado = new TextField();
//        tipoEmpleado.setCaption("Introduzca tipo empleado:");

        List<String> tipoEmpleado = new ArrayList<>();
        tipoEmpleado.add("vendedor");
        tipoEmpleado.add("encargado");

        ComboBox<String> select =
                new ComboBox<>("Seleccione tipo empleado");
        select.setItems(tipoEmpleado);

        final TextField ventasMes = new TextField();
        ventasMes.setCaption("Introduzca ventas mes:");

        final TextField horasExtra = new TextField();
        horasExtra.setCaption("Introduzca horas extra:");

        // Fin parámetros salario bruto

        // Parámetros salario neto
        final TextField inSalarioBruto = new TextField();
        inSalarioBruto.setCaption("Introduzca salario neto:");

        // Fin parámetros salario neto

        // Botón salario bruto

        Button calculaBruto = new Button("Calcular salario bruto");
        calculaBruto.addClickListener(e -> {

            String tipoEmpleadoIn = select.getValue();
            Float ventasMesIn = Float.parseFloat(ventasMes.getValue());
            Float horasExtraIn = Float.parseFloat(horasExtra.getValue());

            EmpleadoBR empleado = new EmpleadoBR();

            try {
                Float resSalarioBruto = empleado.calculaSalarioBruto(tipoEmpleadoIn, ventasMesIn, horasExtraIn);
                Label labelSalarioBruto = new Label("El salario bruto obtenido es: " + resSalarioBruto + " €");
                resSalarioBrutoLayout.removeAllComponents();
                resSalarioBrutoLayout.addComponent(labelSalarioBruto);
            } catch (BRException brException) {
                Label errorSalarioBruto = new Label(brException.getMessage());
                resSalarioBrutoLayout.removeAllComponents();
                resSalarioBrutoLayout.addComponent(errorSalarioBruto);
                brException.printStackTrace();
            }

        });

        // Fin botón salario bruto

        // Botón salario neto

        Button calculaNeto = new Button("Calcular salario neto");
        calculaNeto.addClickListener(e -> {
           Float salarioBrutoIn = Float.parseFloat(inSalarioBruto.getValue());

           EmpleadoBR empleado = new EmpleadoBR();
            try {
                Float resSalarioNeto = empleado.calculaSalarioNeto(salarioBrutoIn);
                Label labelSalarioNeto = new Label("El salario neto obtenido es: " + resSalarioNeto + " €");
                resSalarioNetoLayout.removeAllComponents();
                resSalarioNetoLayout.addComponent(labelSalarioNeto);
            } catch (BRException brException) {
                Label labelSalarioNeto = new Label(brException.getMessage());
                resSalarioNetoLayout.removeAllComponents();
                resSalarioNetoLayout.addComponent(labelSalarioNeto);
                brException.printStackTrace();
            }

        });

        // Fin botón salario bruto

        salarioBruto.addComponents(select, ventasMes, horasExtra);
        salarioBrutoContainer.addComponents(salarioBruto, calculaBruto, resSalarioBrutoLayout);

        salarioNeto.addComponent(inSalarioBruto);
        salarioNetoContainer.addComponents(salarioNeto, calculaNeto, resSalarioNetoLayout);


        tabsheet.addTab(salarioBrutoContainer, "Cálculo salario bruto");
        tabsheet.addTab(salarioNetoContainer, "Cálculo del salario neto");

        mainLayout.addComponents(tabsheet);
        
        setContent(mainLayout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
