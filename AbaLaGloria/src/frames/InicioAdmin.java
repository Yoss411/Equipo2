/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frames;

import clases.Sesion;
import java.awt.Image;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import menulateral.SideMenuItem;
import paquete.Conexion;

/**
 *
 * @author ayele
 */
public class InicioAdmin extends javax.swing.JFrame {

    Conexion conexion = new Conexion();
    private TableRowSorter<DefaultTableModel> sorter;
    /**
     * Creates new form Inicio
     */
    public InicioAdmin() {
        initComponents();
        cargarUsuariosEnTabla(tabUsu);
        this.setLocationRelativeTo(null); // Pantalla centrada 
    this.setTitle("Pantalla de Inicio- Abarrotes 'La Gloria'");
    ImageIcon icono=new ImageIcon(getClass().getResource("/imagenes/fondoAba.jpg"));
        Image imagenEscalada=icono.getImage().getScaledInstance(935, 700, Image.SCALE_SMOOTH);
        ImageIcon iconoFinal = new ImageIcon(imagenEscalada);
        lblFondo.setIcon(iconoFinal);

    
            for (SideMenuItem item : sideMenu.getModel().getItems()) {
        if ("Configuración".equals(item.getText())) { // Buscamos el padre
            System.out.println("Clase del item: " + item.getClass().getName());
System.out.println("Clase del child: " + item.getChildren().get(0).getClass().getName());

            for (SideMenuItem child : item.getChildren()) {
                if ("Mi Perfil".equals(child.getText())) {

                    // Agregamos el actionListener directamente al child
                    sideMenu.setMenuItemAction("Mi Perfil", e -> {
                        int rol = Sesion.getRol(); // obtener el rol actual de sesión

                        if (rol == 1) { // Suponiendo que el ID de administrador es 4
                            new MiPerfil().setVisible(true);
                            this.dispose();
                        } else {
                            child.setEnabled(false); // Deshabilita
                            child.setShown(false);   // Oculta
                        }
                    });

                    // Puedes deshabilitarlo directamente aquí si el rol no es 4
                    if (Sesion.getRol() != 1) {
                        child.setEnabled(false);
                        child.setShown(false);
                    }

                    break;
                }
            }
            break; // ya encontramos "Mi Perfil", salir del primer for
        }
    }
    
    
    
    sideMenu.setMenuItemAction("Productos", e -> {
                        new Producto().setVisible(true);
                        this.dispose();
                    });
    
     sideMenu.setMenuItemAction("Inicio", e -> {
                        new InicioAdmin().setVisible(true);
                        this.dispose();
                    });
     
      sideMenu.setMenuItemAction("Proveedores", e -> {
                        new Proveedor().setVisible(true);
                        this.dispose();
                    });
      
       sideMenu.setMenuItemAction("Salir", e -> {
                        new Login().setVisible(true);
                        this.dispose();
                    });
         
        sideMenu.setMenuItemAction("Compras", e -> {
                        new Compra().setVisible(true);
                        this.dispose();
                    });
        
        sideMenu.setMenuItemAction("Ventas", e -> {
                        new Venta().setVisible(true);
                        this.dispose();
                    });
        
        sideMenu.setMenuItemAction("Ayuda", e -> {
                        new Ayuda().setVisible(true);
                        this.dispose();
                    });
        
        ImageIcon icon=new ImageIcon(getClass().getResource("/imagenes/buscar.png"));
        Image imagenEsc=icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon iconoFin = new ImageIcon(imagenEsc);
        lblBuscar.setIcon(iconoFin);
        
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/imagenes/agregar.png"));
        Image imagen = iconoOriginal.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon iconoRegistrar = new ImageIcon(imagen);
        btnAgrUsu.setIcon(iconoRegistrar);
        btnAgrUsu.setText("Agregar Usuario");
        btnAgrUsu.setHorizontalTextPosition(SwingConstants.RIGHT);  // Texto a la derecha del ícono
        btnAgrUsu.setVerticalTextPosition(SwingConstants.CENTER); 
        
        ImageIcon iconoA = new ImageIcon(getClass().getResource("/imagenes/suspendido.png"));
        Image imagenAct = iconoA.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon iconoEditar = new ImageIcon(imagenAct);
        btnSusUsu.setIcon(iconoEditar);
        btnSusUsu.setText("Suspender Usuario");
        btnSusUsu.setHorizontalTextPosition(SwingConstants.RIGHT);  // Texto a la derecha del ícono
        btnSusUsu.setVerticalTextPosition(SwingConstants.CENTER); 
        
        ImageIcon iconoB = new ImageIcon(getClass().getResource("/imagenes/vista.png"));
        Image imagenVer = iconoB.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon iconoVer = new ImageIcon(imagenVer);
        btnVerInfo.setIcon(iconoVer);
        btnVerInfo.setText("Ver información del usuario");
        btnVerInfo.setHorizontalTextPosition(SwingConstants.RIGHT);  // Texto a la derecha del ícono
        btnVerInfo.setVerticalTextPosition(SwingConstants.CENTER); 
        
    }
    
    public void cargarUsuariosEnTabla(JTable tablaUsuarios) {
    DefaultTableModel modelo = (DefaultTableModel) tablaUsuarios.getModel();
    modelo.setRowCount(0); // Limpiar filas existentes

    try {
        ResultSet rsUsuarios = conexion.consultarTodo("usuario");

        while (rsUsuarios != null && rsUsuarios.next()) {
            int idUsuario = rsUsuarios.getInt("id_usuario");
            String nombreUsu = rsUsuarios.getString("nombre_usuario");
            // ✅ Concatenar nombre completo correctamente
            String nombre = rsUsuarios.getString("nombres");
            String apellidoP = rsUsuarios.getString("apellido_paterno");
            String apellidoM = rsUsuarios.getString("apellido_materno");
            String nombreCompleto = nombre + " " + apellidoP + " " + apellidoM;

            int idRol = rsUsuarios.getInt("id_rol");
            int estadoInt = rsUsuarios.getInt("estado");

            // Obtener nombre del rol
            String nombreRol = "DESCONOCIDO";
            ResultSet rsRol = conexion.buscarPorId("rol", "id_rol", idRol);
            if (rsRol != null && rsRol.next()) {
                nombreRol = rsRol.getString("nombre_rol");
            }
            if (rsRol != null) rsRol.close();

            String estado = (estadoInt == 1) ? "ACTIVO" : "INACTIVO";
            Object[] fila = { idUsuario, nombreUsu, nombreCompleto, nombreRol, estado };
            modelo.addRow(fila);
        }

        if (rsUsuarios != null) rsUsuarios.close();

    } catch (SQLException e) {
        System.err.println("Error al cargar usuarios: " + e.getMessage());
    }

    // Asignar sorter correctamente
    sorter = new TableRowSorter<>(modelo);
    tablaUsuarios.setRowSorter(sorter);
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        sideMenu = new menulateral.SideMenuComponent();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabUsu = new javax.swing.JTable();
        btnVerInfo = new javax.swing.JButton();
        btnAgrUsu = new javax.swing.JButton();
        btnSusUsu = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        lblBuscar = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbFiltro = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        lblFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(164, 158, 126));

        sideMenu.setBackgroundColor(new java.awt.Color(228, 255, 212));
        sideMenu.setHoverColor(new java.awt.Color(228, 255, 212));
        sideMenu.setModel(new menulateral.SideMenuModel() {{ addItem(new menulateral.SideMenuItem("Inicio", "C:\\Users\\ayele\\Downloads\\hogar.png", "Ir a Inicio")); addItem(new menulateral.SideMenuItem("Productos", "C:\\Users\\ayele\\Downloads\\abarrotes-tienda.png", "Ir al almacén")); addItem(new menulateral.SideMenuItem("Proveedores", "C:\\Users\\ayele\\Downloads\\gestion-de-la-cadena-de-suministro.png", "Ir a proveedores ")); addItem(new menulateral.SideMenuItem("Compras", "C:\\Users\\ayele\\Downloads\\agregar-producto.png", "Ir a realizar compras")); addItem(new menulateral.SideMenuItem("Ventas", "C:\\Users\\ayele\\Downloads\\carrito-de-compras (1).png", "Ir a realizar ventas ")); addItem(new menulateral.SideMenuItem("Configuración", "C:\\Users\\ayele\\Downloads\\configuracion-del-usuario.png", "Ir a configuración")); menulateral.SideMenuItem item9153 = getItem(5); menulateral.SideMenuItem item8188 = new menulateral.SideMenuItem("Mi Perfil", "C:\\Users\\ayele\\Downloads\\avatar-de-usuario.png", "Ir a mi perfil"); item9153.addChild(item8188); addItem(new menulateral.SideMenuItem("Ayuda", "C:\\Users\\ayele\\Downloads\\ayuda.png", "Ayuda")); addItem(new menulateral.SideMenuItem("Salir", "C:\\Users\\ayele\\Downloads\\cerrar sesion (1).png", "Ir a login")); }});

        jPanel2.setBackground(new java.awt.Color(110, 143, 133));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 675, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("VENTAS", jPanel2);

        jPanel3.setBackground(new java.awt.Color(110, 143, 133));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 675, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("COMPRAS", jPanel3);

        jPanel4.setBackground(new java.awt.Color(110, 143, 133));

        tabUsu.setBackground(new java.awt.Color(235, 231, 167));
        tabUsu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID USUARIO", "NOMBRE USUARIO", "NOMBRE COMPLETO", "ROL", "ESTATUS"
            }
        ));
        jScrollPane1.setViewportView(tabUsu);

        btnVerInfo.setBackground(new java.awt.Color(237, 198, 142));
        btnVerInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerInfoActionPerformed(evt);
            }
        });

        btnAgrUsu.setBackground(new java.awt.Color(237, 198, 142));
        btnAgrUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgrUsuActionPerformed(evt);
            }
        });

        btnSusUsu.setBackground(new java.awt.Color(237, 198, 142));
        btnSusUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSusUsuActionPerformed(evt);
            }
        });

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });

        jLabel2.setText("Filtrar por:");

        cmbFiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre: A-Z", "Nombre: Z-A", "Estado", "Rol" }));
        cmbFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFiltroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnVerInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnAgrUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSusUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(cmbFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAgrUsu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSusUsu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnVerInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addGap(90, 90, 90))
        );

        jTabbedPane1.addTab("USUARIOS", jPanel4);

        jPanel5.setBackground(new java.awt.Color(110, 143, 133));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 675, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CLIENTES/PROVEEDORES", jPanel5);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sideMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 675, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(138, 138, 138))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(sideMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 0, 1030, 730));
        getContentPane().add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 940, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVerInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerInfoActionPerformed
        // TODO add your handling code here:
        int filaSeleccionada = tabUsu.getSelectedRow();

    if (filaSeleccionada != -1) {
        // Obtener el ID del usuario seleccionado (asumiendo que está en la columna 0)
        int idUsuario = (int) tabUsu.getValueAt(filaSeleccionada, 0);

        // Abrir la ventana VerUsuario con el ID
        VerUsuario ventanaVer = new VerUsuario(idUsuario);
        ventanaVer.setVisible(true);

        this.dispose(); // Cierra la ventana actual si todo va bien
    } else {
        JOptionPane.showMessageDialog(this, "Por favor selecciona un usuario para ver.");
    }
    }//GEN-LAST:event_btnVerInfoActionPerformed

    private void btnAgrUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgrUsuActionPerformed
        // TODO add your handling code here:
        new RegistrarUsuario().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAgrUsuActionPerformed

    private void btnSusUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSusUsuActionPerformed
        // TODO add your handling code here:
         int fila = tabUsu.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Selecciona un usuario primero.");
        return;
    }

    int idUsuario = (int) tabUsu.getValueAt(fila, 0); // Columna 0 = id
    String estadoActual = tabUsu.getValueAt(fila, 4).toString(); // Columna 3 = ACTIVO/INACTIVO
System.out.println("Estado leído en tabla (columna 4): '" + estadoActual + "'");

    int nuevoEstado = estadoActual.equalsIgnoreCase("ACTIVO") ? 0 : 1;
    String mensajeConfirmacion = estadoActual.equalsIgnoreCase("ACTIVO")
            ? "¿Deseas suspender al usuario?"
            : "El usuario está suspendido. ¿Deseas activarlo de nuevo?";

    int respuesta = JOptionPane.showConfirmDialog(this, mensajeConfirmacion, "Confirmar", JOptionPane.YES_NO_OPTION);

    if (respuesta == JOptionPane.YES_OPTION) {
        boolean actualizado = conexion.actualizar(
            "usuario",
            "estado = ?",
            "id_usuario = ?",
            nuevoEstado,
            idUsuario
        );

        if (actualizado) {
            String mensajeExito = (nuevoEstado == 0) ? "Usuario suspendido." : "Usuario activado.";
            JOptionPane.showMessageDialog(this, mensajeExito);

            // Refrescar tabla
            cargarUsuariosEnTabla(tabUsu);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado.");
        }
    }
    }//GEN-LAST:event_btnSusUsuActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        // TODO add your handling code here:
        String texto = txtBuscar.getText().trim();

        if (texto.length() == 0) {
            sorter.setRowFilter(null); // Mostrar todo
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto)); // Filtrado con ignore case
        }
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void cmbFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFiltroActionPerformed
        // TODO add your handling code here:
        aplicarFiltroYOrden();
    }//GEN-LAST:event_cmbFiltroActionPerformed

     private void aplicarFiltroYOrden() {
    String opcion = (String) cmbFiltro.getSelectedItem();

    if (sorter == null) return;

    switch (opcion) {
        case "Nombre: A-Z":
            sorter.setSortKeys(Collections.singletonList(
                new RowSorter.SortKey(1, SortOrder.ASCENDING) // columna 1 = Nombre
            ));
            sorter.sort();
            break;

        case "Nombre: Z-A":
            sorter.setSortKeys(Collections.singletonList(
                new RowSorter.SortKey(1, SortOrder.DESCENDING)
            ));
            sorter.sort();
            break;

        case "Rol":
            sorter.setSortKeys(Collections.singletonList(
                new RowSorter.SortKey(3, SortOrder.ASCENDING) // columna 3 = Rol
            ));
            sorter.sort();
            break;

        case "Estado":
            sorter.setSortKeys(Collections.singletonList(
                new RowSorter.SortKey(4, SortOrder.ASCENDING) // columna 4 = Estado
            ));
            sorter.sort();
            break;

        default:
            sorter.setSortKeys(null);
            break;
    }
}
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InicioAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InicioAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InicioAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InicioAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InicioAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgrUsu;
    private javax.swing.JButton btnSusUsu;
    private javax.swing.JButton btnVerInfo;
    private javax.swing.JComboBox<String> cmbFiltro;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane19;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblFondo;
    private menulateral.SideMenuComponent sideMenu;
    private javax.swing.JTable tabUsu;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
