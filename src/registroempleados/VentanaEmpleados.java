package registroempleados;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class VentanaEmpleados extends JFrame {

    private final JTextField txtId = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtCargo = new JTextField();
    private final JTextField txtSalario = new JTextField();
    private final JTextField txtCorreo = new JTextField();

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Cargo", "Salario", "Correo"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private final EmpleadoDAO dao = new EmpleadoDAO();

    public VentanaEmpleados() {
        setTitle("Registro de Empleados");
        setSize(700, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(5, 2, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Datos del empleado"));
        txtId.setEditable(false);
        form.add(new JLabel("ID:"));      form.add(txtId);
        form.add(new JLabel("Nombre:"));  form.add(txtNombre);
        form.add(new JLabel("Cargo:"));   form.add(txtCargo);
        form.add(new JLabel("Salario:")); form.add(txtSalario);
        form.add(new JLabel("Correo:"));  form.add(txtCorreo);

        JPanel botones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        botones.add(btnGuardar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarFila();
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        try {
            for (Empleado e : dao.listar()) {
                modelo.addRow(new Object[]{
                    e.getId(), e.getNombre(), e.getCargo(), e.getSalario(), e.getCorreo()});
            }
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void guardar() {
        Empleado emp = leerFormulario();
        if (emp == null) {
            return;
        }
        try {
            dao.crear(emp);
            limpiar();
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Empleado guardado");
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void actualizar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un empleado de la tabla");
            return;
        }
        Empleado emp = leerFormulario();
        if (emp == null) {
            return;
        }
        emp.setId(Integer.parseInt(txtId.getText()));
        try {
            dao.actualizar(emp);
            limpiar();
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Empleado actualizado");
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void eliminar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un empleado de la tabla");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar este empleado?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.eliminar(Integer.parseInt(txtId.getText()));
            limpiar();
            cargarTabla();
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private Empleado leerFormulario() {
        String nombre = txtNombre.getText().trim();
        String cargo = txtCargo.getText().trim();
        String correo = txtCorreo.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
            return null;
        }
        if (!correo.isEmpty() && !correo.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            JOptionPane.showMessageDialog(this, "El correo no es válido");
            return null;
        }
        double salario;
        try {
            salario = Double.parseDouble(txtSalario.getText().trim());
            if (salario < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El salario debe ser un número positivo");
            return null;
        }
        return new Empleado(0, nombre, cargo, salario, correo);
    }

    private void seleccionarFila() {
        int f = tabla.getSelectedRow();
        if (f < 0) {
            return;
        }
        txtId.setText(String.valueOf(modelo.getValueAt(f, 0)));
        txtNombre.setText(String.valueOf(modelo.getValueAt(f, 1)));
        txtCargo.setText(valorONada(modelo.getValueAt(f, 2)));
        txtSalario.setText(String.valueOf(modelo.getValueAt(f, 3)));
        txtCorreo.setText(valorONada(modelo.getValueAt(f, 4)));
    }

    private String valorONada(Object o) {
        return o == null ? "" : o.toString();
    }

    private void limpiar() {
        txtId.setText("");
        txtNombre.setText("");
        txtCargo.setText("");
        txtSalario.setText("");
        txtCorreo.setText("");
        tabla.clearSelection();
    }

    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this,
                "Error con la base de datos:\n" + ex.getMessage()
                + "\n\n¿XAMPP (MySQL) está encendido y creaste la BD registro_empleados?",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaEmpleados().setVisible(true));
    }
}