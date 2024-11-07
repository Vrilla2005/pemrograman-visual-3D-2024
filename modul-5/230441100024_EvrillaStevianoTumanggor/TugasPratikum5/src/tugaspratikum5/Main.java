/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tugaspratikum5;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author evril
 */
public class Main extends javax.swing.JFrame {

    Connection conn;
    
    private DefaultTableModel karyawan;
    private DefaultTableModel proyek;
    private DefaultTableModel transaksiproyek;
    
    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        this.setLocationRelativeTo(null);
        conn = koneksi.getConnection(); 
        
        tabelKaryawan();
        tabelProyek();
        tabelTransaksi();
        
        loadDataKaryawan();
        loadDataProyek();
        LoadDataTransaksi();
        loadComboBoxKaryawan();
        loadComboBoxProyek();

        KeyListener keyListener = new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                checkButtonKaryawan();
                checkButtonProyek();
                checkButtonTransaksi();
            }
        };
        
        
//        txt1karyawan1.addKeyListener(keyListener);
        txt1karyawan1.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); 
                    JOptionPane.showMessageDialog(null, "ID wajib berupa angka!");
                }
            }
        });
        txt1karyawan2.addKeyListener(keyListener);
        txt1karyawan3.addKeyListener(keyListener);
        txt1karyawan4.addKeyListener(keyListener);
        
        txt2proyek1.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); 
                    JOptionPane.showMessageDialog(null, "ID wajib berupa angka!");
                }
            }
        });
        txt2proyek2.addKeyListener(keyListener);
        txt2proyek3.addKeyListener(keyListener);
        
        cbkaryawan.addKeyListener(keyListener);
        cbproyek.addKeyListener(keyListener);
        txt3transaksi3.addKeyListener(keyListener);
         
        checkButtonKaryawan(); 
        checkButtonProyek();
        checkButtonTransaksi();
    }
    
    private void checkButtonKaryawan() {
        boolean Isi_ID = !txt1karyawan1.getText().trim().isEmpty();
        boolean Isi_nama = !txt1karyawan2.getText().trim().isEmpty();
        boolean Isi_departemen = !txt1karyawan3.getText().trim().isEmpty();
        boolean Isi_jabatan = !txt1karyawan4.getText().trim().isEmpty();
        
        if (Isi_ID && !Isi_nama && !Isi_departemen && !Isi_jabatan) {
            btndelete.setEnabled(true);
            btnupdate.setEnabled(false);
            btnsimpan.setEnabled(false);
        } else if (Isi_ID && Isi_nama && Isi_departemen && Isi_jabatan) {
            btndelete.setEnabled(false);
            btnupdate.setEnabled(true);
            btnsimpan.setEnabled(false);
        } else if (!Isi_ID && Isi_nama && Isi_departemen && Isi_jabatan) {
            btndelete.setEnabled(false);
            btnupdate.setEnabled(false);
            btnsimpan.setEnabled(true);
        } else {
            btndelete.setEnabled(false);
            btnupdate.setEnabled(false);
            btnsimpan.setEnabled(false);
        }
    }
    
    private void checkButtonProyek() {
        boolean Isi_id_proyek = !txt2proyek1.getText().trim().isEmpty();
        boolean Isi_nama_proyek = !txt2proyek2.getText().trim().isEmpty();
        boolean Isi_departemen_proyek = !txt2proyek3.getText().trim().isEmpty();
        
        if (Isi_id_proyek && !Isi_nama_proyek && !Isi_departemen_proyek ) {
            btndelete1.setEnabled(true);
            btnupdate1.setEnabled(false);
            btnsimpan1.setEnabled(false);
        } else if (Isi_id_proyek && Isi_nama_proyek && Isi_departemen_proyek ) {
            btndelete1.setEnabled(false);
            btnupdate1.setEnabled(true);
            btnsimpan1.setEnabled(false);
        } else if (!Isi_id_proyek && Isi_nama_proyek && Isi_departemen_proyek ) {
            btndelete1.setEnabled(false);
            btnupdate1.setEnabled(false);
            btnsimpan1.setEnabled(true);
        } else {
            btndelete1.setEnabled(false);
            btnupdate1.setEnabled(false);
            btnsimpan1.setEnabled(false);
        }
    }
    
    private void checkButtonTransaksi() {
        boolean IdK_isi = cbkaryawan.getSelectedItem() != null; 
        boolean idP_isi = cbproyek.getSelectedItem() != null; 
        boolean peran_isi = !txt3transaksi3.getText().trim().isEmpty(); 

        if (IdK_isi && idP_isi && !peran_isi) {
            btndelete2.setEnabled(true);
            btnupdate2.setEnabled(false);
            btnsimpan2.setEnabled(false);
        } else if (IdK_isi && idP_isi && peran_isi) {
            btndelete2.setEnabled(false);
            btnupdate2.setEnabled(true);
            btnsimpan2.setEnabled(true);
        } else {
            btndelete2.setEnabled(false);
            btnupdate2.setEnabled(false);
            btnsimpan2.setEnabled(false);
        }
    }
    
    private void tabelKaryawan(){
        karyawan = new DefaultTableModel();
        tablekaryawan.setModel(karyawan);

        karyawan.addColumn("ID");
        karyawan.addColumn("Nama");
        karyawan.addColumn("Jabatan");
        karyawan.addColumn("Departemen");
    }
    
    private void tabelProyek(){
        proyek = new DefaultTableModel();
        tableproyek.setModel(proyek);

        proyek.addColumn("ID");
        proyek.addColumn("Nama Proyek");
        proyek.addColumn("Durasi Pengerjaan");
    }
    
    private void tabelTransaksi(){
        transaksiproyek = new DefaultTableModel();
        tabletransaksi.setModel(transaksiproyek);

        transaksiproyek.addColumn("Karyawan");
        transaksiproyek.addColumn("Proyek");
        transaksiproyek.addColumn("Durasi");   
    }
    
    private void loadDataKaryawan() {
      karyawan.setRowCount(0);

      try {
          String sql = "SELECT * FROM karyawan";
          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();
          while (rs.next()) {
             karyawan.addRow(new Object[]{
             rs.getInt("id"),
             rs.getString("nama"),
             rs.getString("jabatan"),
             rs.getString("departemen")
           });
          }
        } catch (SQLException e) {
           System.out.println("Error Load Data Karyawan" + e.getMessage());
        }
    }
    
    private void loadDataProyek() {
      proyek.setRowCount(0);
        try{
            String sql = "SELECT * FROM proyek";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               proyek.addRow(new Object[]{
               rs.getInt("id"),
               rs.getString("nama_proyek"),
               String.format("%d Minggu", rs.getInt("durasi_pengerjaan"))
           });
          }
        } catch (SQLException e) {
           System.out.println("Error Load Data Proyek" + e.getMessage());
        }
    }
    
    private void LoadDataTransaksi(){
        transaksiproyek.setRowCount(0);
        try{
            String sql = "SELECT karyawan.nama AS nama_karyawan, proyek.nama_proyek, transaksi.durasi " +
                        "FROM transaksi " +
                        "JOIN karyawan ON transaksi.id_karyawan = karyawan.id " +
                        "JOIN proyek ON transaksi.id_proyek = proyek.id";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               transaksiproyek.addRow(new Object[]{
               rs.getString("nama_karyawan"),
               rs.getString("nama_proyek"),
//               rs.getString("peran")
               String.format("%d Minggu", rs.getInt("durasi"))
             });
            }
        } catch (SQLException e) {
           System.out.println("Error Load Data Transaksi" + e.getMessage());
        }
    }
    
    private void loadComboBoxKaryawan (){
        cbkaryawan.removeAllItems();
        try {
            String sql = "SELECT id, nama, jabatan, departemen FROM karyawan";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
        
            while (rs.next()) {
                String idNama = rs.getInt("id") + " - " + rs.getString("nama");
//                String idNama = String.format("ID : %d - %s (%s, %s)",
//                rs.getInt("id"),
//                rs.getString("nama"),
//                rs.getString("jabatan"),
//                rs.getString("departemen")
//                );
                cbkaryawan.addItem(idNama); 
            }
        } catch (SQLException e) {
            System.out.println("Error loading Karyawan ComboBox: " + e.getMessage());
        }
    }
    
    private void loadComboBoxProyek(){
        cbproyek.removeAllItems(); 
        try {
            String sql = "SELECT id, nama_proyek, durasi_pengerjaan FROM proyek";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String idNamaProyek = rs.getInt("id") + " - " + rs.getString("nama_proyek"); 
//                String idNamaProyek = String.format("ID : %d - %s (%s)",
//                rs.getInt("id"),
//                rs.getString("nama_proyek"),
//                String.format("%d Minggu", rs.getInt("durasi_pengerjaan"))
//                );
                cbproyek.addItem(idNamaProyek); 
            }
        } catch (SQLException e) {
            System.out.println("Error loading Proyek ComboBox: " + e.getMessage());
        }    
    }
    
    private void saveDataKaryawan() {
        try {
            String sql = "INSERT INTO karyawan (nama, jabatan, departemen) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txt1karyawan2.getText());
            ps.setString(2, txt1karyawan3.getText());
            ps.setString(3, txt1karyawan4.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data karyawan telah ditambah!");
            loadDataKaryawan();
            loadComboBoxKaryawan();
        } catch (SQLException e) {
            System.out.println("Error Save Data Karyawan" + e.getMessage());
        }
    }

    private void updateDataKaryawan() {
        try {
            String sql = "UPDATE karyawan SET nama = ?, jabatan = ?, departemen = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txt1karyawan2.getText());
            ps.setString(2, txt1karyawan3.getText());
            ps.setString(3, txt1karyawan4.getText());
            ps.setInt(4, Integer.parseInt(txt1karyawan1.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data karyawan telah diupdate!");
            loadDataKaryawan();
        }  catch (SQLException e) {
            System.out.println("Error Update Data Karyawan" + e.getMessage());
        }
    }
    
    private void deleteDataKaryawan() {
        try {
            String sql = "DELETE FROM karyawan WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(txt1karyawan1.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data karyawan telah dihapus!");
            loadDataKaryawan();
        } catch (SQLException e) {
            System.out.println("Error Delete Data" + e.getMessage());
        }
    }
    
    
    
    private void saveDataProyek() {
        try {
            String sql = "INSERT INTO proyek (nama_proyek, durasi_pengerjaan) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txt2proyek2.getText());
            ps.setInt(2, Integer.parseInt(txt2proyek3.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data proyek telah ditambah!");
            //
            loadDataProyek();
            loadComboBoxProyek();
        } catch (SQLException e) {
            System.out.println("Error Save Data Proyek" + e.getMessage());
        }
     }

    private void updateDataProyek() {
        try {
            String sql = "UPDATE proyek SET nama_proyek = ?, durasi_pengerjaan = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txt2proyek2.getText());
            ps.setInt(2, Integer.parseInt(txt2proyek3.getText()));
            ps.setInt(3, Integer.parseInt(txt2proyek1.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data proyek telah diupdate!");
            loadDataProyek();
        }  catch (SQLException e) {
            System.out.println("Error Update Data Proyek" + e.getMessage());
        }
     }

    private void deleteDataProyek() {
        try {
            String sql = "DELETE FROM proyek WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(txt2proyek1.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data proyek telah dihapus!");
            loadDataProyek();
        } catch (SQLException e) {
            System.out.println("Error Delete Data Proyek" + e.getMessage());
        }
    }

    private void saveDataTransaksi() {
        String karyawanData = (String) cbkaryawan.getSelectedItem();
        String proyekData = (String) cbproyek.getSelectedItem();
        
        int karyawanID = Integer.parseInt(karyawanData.split(" - ")[0].trim());
        int proyekID = Integer.parseInt(proyekData.split(" - ")[0].trim());
        
//        if (txt3transaksi3.getText().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "ISI SEMUA DATA!", "Input Error", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
        try {
            String sql = "INSERT INTO transaksi (id_karyawan, id_proyek, durasi) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, karyawanID);  
            ps.setInt(2, proyekID);     
//            ps.setString(3, txt3transaksi3.getText());
            ps.setInt(3, Integer.parseInt(txt3transaksi3.getText()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data transaksi telah ditambah!");
            LoadDataTransaksi(); 
        } catch (SQLException e) {
            System.out.println("Error saat menambahkan data transaksi: " + e.getMessage());
        }
    }
    
    private void updateDataTransaksi() {
        if (cbkaryawan.getSelectedItem() == null || cbproyek.getSelectedItem() == null || txt3transaksi3.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih ID Karyawan, ID Proyek, dan isi peran untuk mengupdate transaksi!", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String sql = "UPDATE transaksi SET durasi = ? WHERE id_karyawan = ? AND id_proyek = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            
//            ps.setString(1, txt3transaksi3.getText());
            ps.setInt(1, Integer.parseInt(txt3transaksi3.getText()));
            
            String selectedKaryawan = cbkaryawan.getSelectedItem().toString();
            String selectedProyek = cbproyek.getSelectedItem().toString();
            
            int idKaryawan = Integer.parseInt(selectedKaryawan.split(" - ")[0].trim());
            int idProyek = Integer.parseInt(selectedProyek.split(" - ")[0].trim());
            
            ps.setInt(2, idKaryawan);
            ps.setInt(3, idProyek);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Data transaksi telah diupdate!");
            } else {
                JOptionPane.showMessageDialog(this, "Transaksi tidak ditemukan!", "Update Data", JOptionPane.WARNING_MESSAGE);
            }
            LoadDataTransaksi(); 
        } catch (SQLException e) {
            System.out.println("Error saat mengupdate data transaksi: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error konversi ID: " + e.getMessage());
        }
    }
    
    private void deleteDataTransaksi() {
        if (cbkaryawan.getSelectedItem() == null || cbproyek.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih ID Karyawan dan ID Proyek untuk menghapus transaksi!", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String sql = "DELETE FROM transaksi WHERE id_karyawan = ? AND id_proyek = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            String selectedKaryawan = cbkaryawan.getSelectedItem().toString();
            String selectedProyek = cbproyek.getSelectedItem().toString();
            
            int idKaryawan = Integer.parseInt(selectedKaryawan.split(" - ")[0].trim());
            int idProyek = Integer.parseInt(selectedProyek.split(" - ")[0].trim());
            
            ps.setInt(1, idKaryawan);
            ps.setInt(2, idProyek);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Data transaksi telah dihapus!");
            } else {
                JOptionPane.showMessageDialog(this, "Transaksi tidak ditemukan!", "Hapus Data", JOptionPane.WARNING_MESSAGE);
            }
            LoadDataTransaksi(); 
        } catch (SQLException e) {
            System.out.println("Error saat menghapus data transaksi: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error konversi ID: " + e.getMessage());
        }
    }
    
    private void Exit(){
        int exit = JOptionPane.showConfirmDialog(null,"Apakah anda yakin untuk keluar?","Keluar",JOptionPane.YES_NO_OPTION);
        if (exit == JOptionPane.YES_OPTION) {
            System.exit(0);
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        Karyawan = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnreset1 = new javax.swing.JButton();
        btnkeluar1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt1karyawan1 = new javax.swing.JTextField();
        txt1karyawan2 = new javax.swing.JTextField();
        txt1karyawan3 = new javax.swing.JTextField();
        txt1karyawan4 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablekaryawan = new javax.swing.JTable();
        btnsimpan = new javax.swing.JButton();
        btnupdate = new javax.swing.JButton();
        btndelete = new javax.swing.JButton();
        Proyek = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnreset3 = new javax.swing.JButton();
        btnkeluar3 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt2proyek1 = new javax.swing.JTextField();
        txt2proyek2 = new javax.swing.JTextField();
        txt2proyek3 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableproyek = new javax.swing.JTable();
        btnsimpan1 = new javax.swing.JButton();
        btnupdate1 = new javax.swing.JButton();
        btndelete1 = new javax.swing.JButton();
        TransaksiProyek = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        btnkeluar2 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txt3transaksi3 = new javax.swing.JTextField();
        scrollpane = new javax.swing.JScrollPane();
        tabletransaksi = new javax.swing.JTable();
        btnsimpan2 = new javax.swing.JButton();
        btnupdate2 = new javax.swing.JButton();
        btndelete2 = new javax.swing.JButton();
        cbkaryawan = new javax.swing.JComboBox<>();
        cbproyek = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBackground(new java.awt.Color(105, 117, 101));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        Karyawan.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(49, 54, 63));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tugaspratikum5/karyawan-removebg-preview (1).png"))); // NOI18N
        jPanel1.add(jLabel17);

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tugaspratikum5/360_F_767482095_ysXRjbdJvLS0sw2cX0erwPdzIx0uK58W-removebg-preview (1).png"))); // NOI18N
        jPanel1.add(jLabel12);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("KARYAWAN");
        jPanel1.add(jLabel1);

        Karyawan.add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(49, 54, 63));
        jPanel2.setPreferredSize(new java.awt.Dimension(509, 40));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnreset1.setBackground(new java.awt.Color(204, 220, 174));
        btnreset1.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        btnreset1.setText("Reset");
        btnreset1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnreset1ActionPerformed(evt);
            }
        });
        jPanel2.add(btnreset1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, -1, -1));

        btnkeluar1.setBackground(new java.awt.Color(204, 220, 174));
        btnkeluar1.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        btnkeluar1.setText("Keluar");
        btnkeluar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnkeluar1ActionPerformed(evt);
            }
        });
        jPanel2.add(btnkeluar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, -1, -1));

        Karyawan.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jPanel3.setBackground(new java.awt.Color(118, 171, 174));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("ID ");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Nama");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Jabatan");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Departemen");

        txt1karyawan1.setFont(new java.awt.Font("Times New Roman", 2, 12)); // NOI18N

        txt1karyawan2.setFont(new java.awt.Font("Times New Roman", 2, 12)); // NOI18N

        txt1karyawan3.setFont(new java.awt.Font("Times New Roman", 2, 12)); // NOI18N

        txt1karyawan4.setFont(new java.awt.Font("Times New Roman", 2, 12)); // NOI18N

        tablekaryawan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablekaryawan);

        btnsimpan.setBackground(new java.awt.Color(204, 220, 174));
        btnsimpan.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        btnsimpan.setText("Simpan");
        btnsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpanActionPerformed(evt);
            }
        });

        btnupdate.setBackground(new java.awt.Color(204, 220, 174));
        btnupdate.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        btnupdate.setText("Update");
        btnupdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnupdateActionPerformed(evt);
            }
        });

        btndelete.setBackground(new java.awt.Color(204, 220, 174));
        btndelete.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        btndelete.setText("Delete");
        btndelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt1karyawan4, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                            .addComponent(txt1karyawan3, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                            .addComponent(txt1karyawan2, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                            .addComponent(txt1karyawan1)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnsimpan)
                            .addComponent(btnupdate)
                            .addComponent(btndelete))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt1karyawan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt1karyawan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt1karyawan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt1karyawan4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnsimpan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnupdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btndelete))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Karyawan.add(jPanel3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Karyawan", Karyawan);

        Proyek.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(49, 54, 63));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tugaspratikum5/proyek-removebg-preview (1).png"))); // NOI18N
        jPanel4.add(jLabel13);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("PROYEK");
        jPanel4.add(jLabel6);

        Proyek.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel5.setBackground(new java.awt.Color(49, 54, 63));
        jPanel5.setPreferredSize(new java.awt.Dimension(548, 40));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnreset3.setBackground(new java.awt.Color(204, 220, 174));
        btnreset3.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        btnreset3.setText("Reset");
        btnreset3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnreset3ActionPerformed(evt);
            }
        });
        jPanel5.add(btnreset3, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, -1, -1));

        btnkeluar3.setBackground(new java.awt.Color(204, 220, 174));
        btnkeluar3.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        btnkeluar3.setText("Keluar");
        btnkeluar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnkeluar3ActionPerformed(evt);
            }
        });
        jPanel5.add(btnkeluar3, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, -1, -1));

        Proyek.add(jPanel5, java.awt.BorderLayout.PAGE_END);

        jPanel6.setBackground(new java.awt.Color(118, 171, 174));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("ID");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Nama Proyek");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Durasi Pengerjaan");

        txt2proyek1.setFont(new java.awt.Font("Times New Roman", 2, 12)); // NOI18N
        txt2proyek1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt2proyek1ActionPerformed(evt);
            }
        });

        txt2proyek2.setFont(new java.awt.Font("Times New Roman", 2, 12)); // NOI18N

        txt2proyek3.setFont(new java.awt.Font("Times New Roman", 2, 12)); // NOI18N

        tableproyek.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tableproyek);

        btnsimpan1.setBackground(new java.awt.Color(204, 220, 174));
        btnsimpan1.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        btnsimpan1.setText("Simpan");
        btnsimpan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpan1ActionPerformed(evt);
            }
        });

        btnupdate1.setBackground(new java.awt.Color(204, 220, 174));
        btnupdate1.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        btnupdate1.setText("Update");
        btnupdate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnupdate1ActionPerformed(evt);
            }
        });

        btndelete1.setBackground(new java.awt.Color(204, 220, 174));
        btndelete1.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        btndelete1.setText("Delete");
        btndelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndelete1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                            .addComponent(txt2proyek3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt2proyek2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(85, 85, 85)
                            .addComponent(txt2proyek1)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnsimpan1)
                            .addComponent(btnupdate1)
                            .addComponent(btndelete1))))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt2proyek1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt2proyek2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txt2proyek3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnsimpan1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnupdate1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btndelete1)))
                .addContainerGap(158, Short.MAX_VALUE))
        );

        Proyek.add(jPanel6, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Proyek", Proyek);

        TransaksiProyek.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(49, 54, 63));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tugaspratikum5/transaksi-removebg-preview (1).png"))); // NOI18N
        jPanel7.add(jLabel16);

        jLabel10.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("TRANSAKSI PROYEK");
        jPanel7.add(jLabel10);

        TransaksiProyek.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jPanel8.setBackground(new java.awt.Color(49, 54, 63));
        jPanel8.setPreferredSize(new java.awt.Dimension(548, 40));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnkeluar2.setBackground(new java.awt.Color(204, 220, 174));
        btnkeluar2.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        btnkeluar2.setText("Keluar");
        btnkeluar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnkeluar2ActionPerformed(evt);
            }
        });
        jPanel8.add(btnkeluar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, -1, -1));

        TransaksiProyek.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        jPanel9.setBackground(new java.awt.Color(118, 171, 174));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Karyawan");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Proyek");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Durasi");

        txt3transaksi3.setFont(new java.awt.Font("Times New Roman", 2, 12)); // NOI18N

        tabletransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrollpane.setViewportView(tabletransaksi);

        btnsimpan2.setBackground(new java.awt.Color(204, 220, 174));
        btnsimpan2.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        btnsimpan2.setText("Simpan");
        btnsimpan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpan2ActionPerformed(evt);
            }
        });

        btnupdate2.setBackground(new java.awt.Color(204, 220, 174));
        btnupdate2.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        btnupdate2.setText("Update");
        btnupdate2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnupdate2ActionPerformed(evt);
            }
        });

        btndelete2.setBackground(new java.awt.Color(204, 220, 174));
        btndelete2.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        btndelete2.setText("Delete");
        btndelete2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndelete2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(scrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnsimpan2)
                            .addComponent(btnupdate2)
                            .addComponent(btndelete2)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt3transaksi3, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                            .addComponent(cbkaryawan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbproyek, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cbkaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(cbproyek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txt3transaksi3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(btnsimpan2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnupdate2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btndelete2)))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        TransaksiProyek.add(jPanel9, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Transaksi Proyek", TransaksiProyek);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnreset1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnreset1ActionPerformed
        // TODO add your handling code here:
        txt1karyawan1.setText("");
        txt1karyawan2.setText("");
        txt1karyawan3.setText("");
        txt1karyawan4.setText("");
    }//GEN-LAST:event_btnreset1ActionPerformed

    private void btnsimpan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpan1ActionPerformed
        // TODO add your handling code here:
        saveDataProyek();
    }//GEN-LAST:event_btnsimpan1ActionPerformed

    private void btnupdate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnupdate1ActionPerformed
        // TODO add your handling code here:
        updateDataProyek();
    }//GEN-LAST:event_btnupdate1ActionPerformed

    private void btndelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndelete1ActionPerformed
        // TODO add your handling code here:
        deleteDataProyek();
    }//GEN-LAST:event_btndelete1ActionPerformed

    private void btnsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpanActionPerformed
        // TODO add your handling code here:
        saveDataKaryawan();
    }//GEN-LAST:event_btnsimpanActionPerformed

    private void btnupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnupdateActionPerformed
        // TODO add your handling code here:
        updateDataKaryawan();
    }//GEN-LAST:event_btnupdateActionPerformed

    private void btndeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndeleteActionPerformed
        // TODO add your handling code here:
        deleteDataKaryawan();
    }//GEN-LAST:event_btndeleteActionPerformed

    private void txt2proyek1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt2proyek1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt2proyek1ActionPerformed

    private void btnreset3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnreset3ActionPerformed
        // TODO add your handling code here:
        txt2proyek1.setText("");
        txt2proyek2.setText("");
        txt2proyek3.setText("");
    }//GEN-LAST:event_btnreset3ActionPerformed

    private void btnkeluar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnkeluar2ActionPerformed
        // TODO add your handling code here:
        Exit();
    }//GEN-LAST:event_btnkeluar2ActionPerformed

    private void btnkeluar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnkeluar3ActionPerformed
        // TODO add your handling code here:
        Exit();
    }//GEN-LAST:event_btnkeluar3ActionPerformed

    private void btnkeluar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnkeluar1ActionPerformed
        // TODO add your handling code here:
        Exit();
    }//GEN-LAST:event_btnkeluar1ActionPerformed

    private void btnsimpan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpan2ActionPerformed
        // TODO add your handling code here:
        saveDataTransaksi();
    }//GEN-LAST:event_btnsimpan2ActionPerformed

    private void btnupdate2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnupdate2ActionPerformed
        // TODO add your handling code here:
        updateDataTransaksi();
    }//GEN-LAST:event_btnupdate2ActionPerformed

    private void btndelete2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndelete2ActionPerformed
        // TODO add your handling code here:
        deleteDataTransaksi();
    }//GEN-LAST:event_btndelete2ActionPerformed

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Karyawan;
    private javax.swing.JPanel Proyek;
    private javax.swing.JPanel TransaksiProyek;
    private javax.swing.JButton btndelete;
    private javax.swing.JButton btndelete1;
    private javax.swing.JButton btndelete2;
    private javax.swing.JButton btnkeluar1;
    private javax.swing.JButton btnkeluar2;
    private javax.swing.JButton btnkeluar3;
    private javax.swing.JButton btnreset1;
    private javax.swing.JButton btnreset3;
    private javax.swing.JButton btnsimpan;
    private javax.swing.JButton btnsimpan1;
    private javax.swing.JButton btnsimpan2;
    private javax.swing.JButton btnupdate;
    private javax.swing.JButton btnupdate1;
    private javax.swing.JButton btnupdate2;
    private javax.swing.JComboBox<String> cbkaryawan;
    private javax.swing.JComboBox<String> cbproyek;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JScrollPane scrollpane;
    private javax.swing.JTable tablekaryawan;
    private javax.swing.JTable tableproyek;
    private javax.swing.JTable tabletransaksi;
    private javax.swing.JTextField txt1karyawan1;
    private javax.swing.JTextField txt1karyawan2;
    private javax.swing.JTextField txt1karyawan3;
    private javax.swing.JTextField txt1karyawan4;
    private javax.swing.JTextField txt2proyek1;
    private javax.swing.JTextField txt2proyek2;
    private javax.swing.JTextField txt2proyek3;
    private javax.swing.JTextField txt3transaksi3;
    // End of variables declaration//GEN-END:variables
}
