/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frames;

import clases.CaptchaPanel;
import clases.Sesion;
import java.awt.Image;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import menulateral.SideMenuItem;
import paquete.Conexion;

/**
 *
 * @author ayele
 */
public class EditarInfoUsu extends javax.swing.JFrame {

    Conexion conexion = new Conexion();
    private int idUsuarioActual ;
private int idDireccionActual ;
    /**
     * Creates new form EditarInfoUsu
     */
    public EditarInfoUsu(int idUsuario) {
        initComponents();
        cargarGeneros();
        this.idUsuarioActual = idUsuario;
         cargarDatosUsuario(idUsuario);
        ImageIcon icono=new ImageIcon(getClass().getResource("/imagenes/editar (1).png"));
        Image imagenEscalada=icono.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon iconoFinal = new ImageIcon(imagenEscalada);
        lblFondo.setIcon(iconoFinal);
        
        ImageIcon iconoA = new ImageIcon(getClass().getResource("/imagenes/usuario (2).png"));
        Image imagenAct = iconoA.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon iconoEditar = new ImageIcon(imagenAct);
        btnGuardar.setIcon(iconoEditar);
        btnGuardar.setText("Guardar Información");
        btnGuardar.setHorizontalTextPosition(SwingConstants.RIGHT);  // Texto a la derecha del ícono
        btnGuardar.setVerticalTextPosition(SwingConstants.CENTER); 
        
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
    }

    private void cargarGeneros() {
    Conexion conn = new Conexion(); // Tu clase personalizada de conexión
    ResultSet rs = conn.showColumnsLike("usuario", "genero"); // Usamos el método genérico

    try {
        comboGenero.removeAllItems();

        if (rs != null && rs.next()) {
            // Obtenemos el valor del tipo ENUM desde la columna 'Type'
            String enumValues = rs.getString("Type");
            // Removemos los paréntesis y dividimos los valores
            enumValues = enumValues.substring(enumValues.indexOf("(") + 1, enumValues.lastIndexOf(")"));
            String[] valores = enumValues.split(",");

            for (String valor : valores) {
                valor = valor.replace("'", "").trim(); // Limpiamos comillas simples y espacios
                comboGenero.addItem(valor); // Agregamos al combo
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Error al cargar los géneros.");
    }
}
    
    private void cargarDatosUsuario(int idUsuario) {
    try {
       
        
        ResultSet rsUsuario = conexion.buscarPorId("usuario", "id_usuario", idUsuario);

        if (rsUsuario != null && rsUsuario.next()) {
            // Datos personales
            txtUsuario.setText(rsUsuario.getString("nombre_usuario"));
            txtNombre.setText(rsUsuario.getString("nombres"));
            txtAPater.setText(rsUsuario.getString("apellido_paterno"));
            txtAMater.setText(rsUsuario.getString("apellido_materno"));
            txtCurp.setText(rsUsuario.getString("curp"));
            txtCelular.setText(rsUsuario.getString("num_celular"));

            // Género
            String genero = rsUsuario.getString("genero");
            comboGenero.setSelectedItem(genero);

            

            // Dirección (ahora con campos separados)
            int idDireccion = rsUsuario.getInt("id_direccion");
            ResultSet rsDir = conexion.buscarPorId("direccion", "id_direccion", idDireccion);
            if (rsDir != null && rsDir.next()) {
                txtCalle.setText(rsDir.getString("calle"));
                txtNum.setText(rsDir.getString("numero"));
                txtColonia.setText(rsDir.getString("colonia")); // Asegúrate de tener esta columna
                txtCiudad.setText(rsDir.getString("ciudad"));
                txtCP.setText(rsDir.getString("cpostal"));
                txtEstado.setText(rsDir.getString("estado"));
                txtPais.setText(rsDir.getString("pais"));
                rsDir.close();
            }

            rsUsuario.close();

        } else {
            JOptionPane.showMessageDialog(this, "⚠️ No se encontró el usuario con ID " + idUsuario);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Error al cargar datos del usuario: " + e.getMessage());
    }
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
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtNum = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtContraseña = new javax.swing.JPasswordField();
        txtCP = new javax.swing.JTextField();
        txtCiudad = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtPais = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtCurp = new javax.swing.JTextField();
        txtUsuario = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtEstado = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtCelular = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtColonia = new javax.swing.JTextField();
        txtAPater = new javax.swing.JTextField();
        sideMenu = new menulateral.SideMenuComponent();
        jLabel4 = new javax.swing.JLabel();
        txtAMater = new javax.swing.JTextField();
        txtCalle = new javax.swing.JTextField();
        comboGenero = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        lblFondo = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        btnRefrescar = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        txtCaptcha = new javax.swing.JTextField();
        captcha = new CaptchaPanel();
        btnGuardar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(164, 158, 126));

        jLabel13.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel13.setText("Calle");

        jLabel14.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel14.setText("Número");

        jLabel16.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel16.setText("Ciudad");

        jLabel17.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel17.setText("Códipo postal");

        txtCP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCPActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel18.setText("Estado");

        jLabel11.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel11.setText("Dirección");

        jLabel19.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel19.setText("País");

        jLabel6.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel6.setText("Nombre de usuario");

        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel7.setText("Contraseña");

        txtCurp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCurpActionPerformed(evt);
            }
        });

        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel15.setText("Curp");

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel3.setText("Apellido Paterno");

        jLabel20.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel20.setText("Número celular");

        jLabel21.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel21.setText("Colonia");

        sideMenu.setBackgroundColor(new java.awt.Color(228, 255, 212));
        sideMenu.setExpanded(true);
        sideMenu.setHoverColor(new java.awt.Color(228, 255, 212));
        sideMenu.setModel(new menulateral.SideMenuModel() {{ addItem(new menulateral.SideMenuItem("Inicio", "C:\\Users\\ayele\\Downloads\\hogar.png", "Ir a Inicio")); addItem(new menulateral.SideMenuItem("Productos", "C:\\Users\\ayele\\Downloads\\abarrotes-tienda.png", "Ir al almacén")); addItem(new menulateral.SideMenuItem("Proveedores", "C:\\Users\\ayele\\Downloads\\gestion-de-la-cadena-de-suministro.png", "Ir a proveedores ")); addItem(new menulateral.SideMenuItem("Compras", "C:\\Users\\ayele\\Downloads\\agregar-producto.png", "Ir a realizar compras")); addItem(new menulateral.SideMenuItem("Ventas", "C:\\Users\\ayele\\Downloads\\carrito-de-compras (1).png", "Ir a realizar ventas ")); addItem(new menulateral.SideMenuItem("Configuración", "C:\\Users\\ayele\\Downloads\\configuracion-del-usuario.png", "Ir a configuración")); menulateral.SideMenuItem item6996 = getItem(5); menulateral.SideMenuItem item8121 = new menulateral.SideMenuItem("Mi Perfil", "C:\\Users\\ayele\\Downloads\\avatar-de-usuario.png", "Ir a mi perfil"); item6996.addChild(item8121); addItem(new menulateral.SideMenuItem("Ayuda", "C:\\Users\\ayele\\Downloads\\ayuda.png", "Ayuda")); addItem(new menulateral.SideMenuItem("Salir", "C:\\Users\\ayele\\Downloads\\cerrar sesion (1).png", "Ir a login")); }});

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel4.setText("Apellido Materno ");

        txtAMater.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAMaterActionPerformed(evt);
            }
        });

        comboGenero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Femenino", "Masculino", "Binario", "Prefiero no responder" }));

        jLabel1.setFont(new java.awt.Font("Perpetua", 0, 18)); // NOI18N
        jLabel1.setText("Editar Información del Usuario");

        jLabel9.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel9.setText("Género");

        jLabel22.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel22.setText("Introducir captcha");

        btnRefrescar.setBackground(new java.awt.Color(237, 198, 142));
        btnRefrescar.setText("Refrescar");
        btnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescarActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel23.setText("Captcha");

        javax.swing.GroupLayout captchaLayout = new javax.swing.GroupLayout(captcha);
        captcha.setLayout(captchaLayout);
        captchaLayout.setHorizontalGroup(
            captchaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        captchaLayout.setVerticalGroup(
            captchaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        btnGuardar.setBackground(new java.awt.Color(237, 198, 142));
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel5.setText("Nombre(s)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(sideMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 198, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblFondo, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addGap(365, 365, 365))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCP, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCalle, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPais, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtColonia, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(captcha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnRefrescar))
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCaptcha, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(58, 58, 58)))
                        .addGap(19, 19, 19))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(273, 273, 273)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtAMater, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtAPater, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                        .addComponent(txtUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCurp, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                        .addComponent(txtContraseña, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCelular))
                    .addContainerGap(645, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sideMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(lblFondo, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(112, 112, 112)
                                .addComponent(jLabel23)
                                .addGap(3, 3, 3)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnRefrescar)
                                    .addComponent(captcha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCaptcha, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(119, 119, 119)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboGenero, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(4, 4, 4)
                                        .addComponent(jLabel13)
                                        .addGap(35, 35, 35))
                                    .addComponent(txtCalle, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtColonia, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addGap(35, 35, 35))
                                    .addComponent(txtCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCP, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addGap(35, 35, 35))
                                    .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPais, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(113, 113, 113)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel3)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtAPater, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(4, 4, 4)
                    .addComponent(txtAMater, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                    .addComponent(jLabel15)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtCurp, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(32, 32, 32)
                    .addComponent(jLabel7)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel20)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(72, 72, 72)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCPActionPerformed

    private void txtCurpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCurpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCurpActionPerformed

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void txtAMaterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAMaterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAMaterActionPerformed

    private void btnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarActionPerformed
        // TODO add your handling code here:
        captcha.generarCaptcha(); // método de tu clase captcha que genera nueva imagen
        txtCaptcha.setText(""); // limpiar campo de texto
    }//GEN-LAST:event_btnRefrescarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        try {
        // Validar campos obligatorios
        if (txtUsuario.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty() ||
            txtAPater.getText().trim().isEmpty() || txtCurp.getText().trim().isEmpty() ||
            txtCelular.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "⚠️ Por favor completa todos los campos obligatorios.");
            return;
        }

        // === 1️⃣ Guardar la dirección ===
        Map<String, Object> datosDireccion = new LinkedHashMap<>();
        datosDireccion.put("calle", txtCalle.getText());
        datosDireccion.put("numero", txtNum.getText());
        datosDireccion.put("ciudad", txtCiudad.getText());
        datosDireccion.put("cpostal", txtCP.getText());
        datosDireccion.put("estado", txtEstado.getText());
        datosDireccion.put("pais", txtPais.getText());

        int idDireccion = 0;

        // Verificar si ya existe una dirección asociada
        if (idDireccionActual > 0) {
            conexion.actualizarPorCampo("direccion", datosDireccion, "id_direccion", idDireccionActual);
            idDireccion = idDireccionActual;
        } else {
            // Insertar nueva dirección
            String columnas = "calle, numero, colonia, ciudad, cpostal, estado, pais";
            String valores = "?, ?, ?, ?, ?, ?";
            boolean exito = conexion.insertar("direccion", columnas, valores,
                    txtCalle.getText(),
                    txtNum.getText(),
                    txtColonia.getText(),
                    txtCiudad.getText(),
                    txtCP.getText(),
                    txtEstado.getText(),
                    txtPais.getText());

            if (!exito) {
                JOptionPane.showMessageDialog(this, "❌ Error al guardar la dirección.");
                return;
            }

            // Obtener el último ID insertado
            ResultSet rs = conexion.ejecutarConsulta("SELECT LAST_INSERT_ID() AS id");
            if (rs.next()) {
                idDireccion = rs.getInt("id");
            }
            rs.close();
        }

        // === 2️⃣ Guardar o actualizar el usuario ===
        Map<String, Object> datosUsuario = new LinkedHashMap<>();
        datosUsuario.put("nombre_usuario", txtUsuario.getText());
        datosUsuario.put("nombres", txtNombre.getText());
        datosUsuario.put("apellido_paterno", txtAPater.getText());
        datosUsuario.put("apellido_materno", txtAMater.getText());
        datosUsuario.put("curp", txtCurp.getText());
        datosUsuario.put("genero", comboGenero.getSelectedItem().toString());
        datosUsuario.put("num_celular", txtCelular.getText());
        datosUsuario.put("id_direccion", idDireccion);
        datosUsuario.put("estado", 1); // Activo por defecto

        if (idUsuarioActual > 0) {
            conexion.actualizarPorCampo("usuario", datosUsuario, "id_usuario", idUsuarioActual);
            JOptionPane.showMessageDialog(this, "✅ Usuario actualizado correctamente.");
        } else {
            String columnas = "nombre_usuario, nombres, apellido_paterno, apellido_materno, curp, genero, num_celular, id_direccion, estado";
            String valores = "?, ?, ?, ?, ?, ?, ?, ?, ?";
            boolean exito = conexion.insertar("usuario", columnas, valores,
                    txtUsuario.getText(),
                    txtNombre.getText(),
                    txtAPater.getText(),
                    txtAMater.getText(),
                    txtCurp.getText(),
                    comboGenero.getSelectedItem().toString(),
                    txtCelular.getText(),
                    idDireccion,
                    1);

            if (exito) {
                JOptionPane.showMessageDialog(this, "✅ Usuario guardado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al guardar el usuario.");
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Error al guardar los datos: " + e.getMessage());
    }
    }//GEN-LAST:event_btnGuardarActionPerformed

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
            java.util.logging.Logger.getLogger(EditarInfoUsu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditarInfoUsu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditarInfoUsu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditarInfoUsu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                int idUsuario = 0;
                new EditarInfoUsu(idUsuario).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnRefrescar;
    private CaptchaPanel captcha;
    private javax.swing.JComboBox<String> comboGenero;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblFondo;
    private menulateral.SideMenuComponent sideMenu;
    private javax.swing.JTextField txtAMater;
    private javax.swing.JTextField txtAPater;
    private javax.swing.JTextField txtCP;
    private javax.swing.JTextField txtCalle;
    private javax.swing.JTextField txtCaptcha;
    private javax.swing.JTextField txtCelular;
    private javax.swing.JTextField txtCiudad;
    private javax.swing.JTextField txtColonia;
    private javax.swing.JPasswordField txtContraseña;
    private javax.swing.JTextField txtCurp;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNum;
    private javax.swing.JTextField txtPais;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
