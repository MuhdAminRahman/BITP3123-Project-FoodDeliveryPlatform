import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class App {

    /* ---------- domain ---------- */
    static class MenuItem {
        String name;
        double price;
        MenuItem(String name, double price) {
            this.name = name;
            this.price = price;
        }
        @Override public String toString() {
            return name + " - RM" + new DecimalFormat("0.00").format(price);
        }
    }

    private static final AtomicInteger orderCounter = new AtomicInteger(1);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::createAndShowGui);
    }

    private static void createAndShowGui() {
        /* ---------- look & feel ---------- */
        Font uiFont = new Font("Poppins", Font.PLAIN, 20);
        UIManager.put("Button.font",  uiFont);
        UIManager.put("Label.font",   uiFont);
        UIManager.put("TextArea.font",uiFont);
        UIManager.put("Table.font",   uiFont);

        JFrame f = new JFrame("SkyBite™ Food Delivery App");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);

        CardLayout cards = new CardLayout();
        JPanel main = new JPanel(cards);

        /* ---------- 1. CHOICE PANEL ---------- */
        JPanel choicePanel = new JPanel(new GridLayout(1,2,40,40));
        JButton dineBtn  = new JButton("Dine-in");
        JButton takeBtn  = new JButton("Take-out");
        choicePanel.setBorder(BorderFactory.createEmptyBorder(100,100,100,100));
        choicePanel.add(dineBtn);
        choicePanel.add(takeBtn);

        /* ---------- 2. ORDER PANEL ---------- */
        JPanel orderPanel = new JPanel(new BorderLayout());

        /* menu list */
        java.util.List<MenuItem> items = java.util.List.of(
                new MenuItem("Nasi Lemak", 5.00),
                new MenuItem("Chicken Rice", 6.00),
                new MenuItem("Burger", 7.00),
                new MenuItem("Teh Tarik", 2.50),
                new MenuItem("Milo", 3.00),
                new MenuItem("Roti Canai", 4.00),
                new MenuItem("Mee Goreng", 5.50),
                new MenuItem("Laksa", 6.50),
                new MenuItem("Satay", 8.00),
                new MenuItem("Cendol", 3.50),
                new MenuItem("Ice Kacang", 4.00),
                new MenuItem("Popiah", 2.00),
                new MenuItem("Roti John", 5.00),
                new MenuItem("Nasi Goreng", 6.00),
                new MenuItem("Mee Rebus", 5.50),
                new MenuItem("Kway Teow", 6.00),
                new MenuItem("Char Kway Teow", 7.00),
                new MenuItem("Nasi Briyani", 8.00),
                new MenuItem("Rendang", 9.00),
                new MenuItem("Sambal Sotong", 10.00),
                new MenuItem("Ayam Penyet", 7.50),
                new MenuItem("Ikan Bakar", 9.50),
                new MenuItem("Keropok Lekor", 3.00),
                new MenuItem("Kuih Muih", 2.50),
                new MenuItem("Soto", 6.00),
                new MenuItem("Nasi Padang", 8.50),
                new MenuItem("Mee Bandung", 7.00),
                new MenuItem("Roti Bakar", 3.50),
                new MenuItem("Kari Ayam", 7.50),
                new MenuItem("Kari Ikan", 8.00),
                new MenuItem("Kari Kambing", 9.00),
                new MenuItem("Mee Soto", 6.50),
                new MenuItem("Nasi Campur", 7.00),
                new MenuItem("Nasi Ulam", 6.00),
                new MenuItem("Nasi Kerabu", 8.00),
                new MenuItem("Nasi Dagang", 8.50),
                new MenuItem("Nasi Tomato", 7.50),
                new MenuItem("Nasi Minyak", 7.00),
                new MenuItem("Nasi Kandar", 9.00),
                new MenuItem("Nasi Lemak Ayam", 8.00),
                new MenuItem("Nasi Goreng Kampung", 6.50),
                new MenuItem("Nasi Goreng Pattaya", 7.50),
                new MenuItem("Nasi Goreng Tom Yum", 7.00),
                new MenuItem("Nasi Goreng Cina", 6.00),
                new MenuItem("Nasi Goreng Kampung Special", 8.00),
                new MenuItem("Nasi Goreng Ikan Masin", 7.50),
                new MenuItem("Nasi Goreng Udang", 8.00),
                new MenuItem("Nasi Goreng Sotong", 8.50),
                new MenuItem("Nasi Goreng Ayam", 7.50),
                new MenuItem("Nasi Goreng Daging", 8.00),
                new MenuItem("Nasi Goreng Kambing", 9.00),
                new MenuItem("Nasi Goreng Seafood", 9.50),
                new MenuItem("Nasi Goreng Kampung Special", 8.50),
                new MenuItem("Nasi Goreng Kampung Ayam", 8.00),
                new MenuItem("Nasi Goreng Kampung Daging", 8.50),
                new MenuItem("Nasi Goreng Kampung Seafood", 9.00),
                new MenuItem("Nasi Goreng Kampung Kambing", 9.50),
                new MenuItem("Nasi Goreng Kampung Sotong", 9.00)
        );
        DefaultListModel<MenuItem> menuModel = new DefaultListModel<>();
        items.forEach(menuModel::addElement);
        JList<MenuItem> menuJList = new JList<>(menuModel);
        menuJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane menuScroll = new JScrollPane(menuJList);

        /* cart table */
        class CartRow {
            MenuItem item; int qty = 1;
            CartRow(MenuItem i){ item=i; }
            @Override
            public String toString() {
                return item.name;
            }
        }
        DefaultTableModel cartModel = new DefaultTableModel(
                new Object[]{"Item","Price","Qty","+","-"},0){
            @Override public boolean isCellEditable(int r,int c){ return c>=3; }
        };
        JTable cartTable = new JTable(cartModel);
        cartTable.setRowHeight(uiFont.getSize()+6);
        cartTable.getColumn("+").setMaxWidth(50);
        cartTable.getColumn("-").setMaxWidth(50);
        JScrollPane cartScroll = new JScrollPane(cartTable);

        /* add to cart */
        JButton addBtn = new JButton("Add to Cart");
        addBtn.addActionListener(e->{
            MenuItem sel = menuJList.getSelectedValue();
            if(sel==null) return;
            for(int i=0;i<cartModel.getRowCount();i++){
                CartRow r=(CartRow)cartModel.getValueAt(i,0);
                if(r.item.name.equals(sel.name)){ r.qty++; cartModel.setValueAt(r.qty,i,2); return; }
            }
            cartModel.addRow(new Object[]{new CartRow(sel),sel.price,1,"+","-"});
        });

        /* +/- buttons in table */
        cartTable.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int r=cartTable.rowAtPoint(e.getPoint());
                int c=cartTable.columnAtPoint(e.getPoint());
                if(r<0) return;
                CartRow row=(CartRow)cartModel.getValueAt(r,0);
                if(c==3){ row.qty++; cartModel.setValueAt(row.qty,r,2); }
                else if(c==4){
                    row.qty--;
                    if(row.qty==0) cartModel.removeRow(r);
                    else cartModel.setValueAt(row.qty,r,2);
                }
            }
        });

        /* utility buttons */
        JButton removeBtn=new JButton("Remove Selected Row");
        removeBtn.addActionListener(e->{
            int r=cartTable.getSelectedRow();
            if(r!=-1) cartModel.removeRow(r);
        });
        JButton clearBtn=new JButton("Clear Cart");
        clearBtn.addActionListener(e->cartModel.setRowCount(0));
        JButton cancelBtn=new JButton("Cancel Order");
        cancelBtn.addActionListener(e->{
            cartModel.setRowCount(0);
            cards.show(main,"choice");
        });
        JButton confirmBtn=new JButton("Confirm Order");
        confirmBtn.addActionListener(e->{
            if(cartModel.getRowCount()==0){
                JOptionPane.showMessageDialog(f,"Cart empty."); return;
            }
            int orderNo=orderCounter.getAndIncrement();
            StringBuilder sb=new StringBuilder("Order #"+orderNo+"\n\n");
            double total=0; DecimalFormat df=new DecimalFormat("0.00");
            for(int i=0;i<cartModel.getRowCount();i++){
                CartRow row=(CartRow)cartModel.getValueAt(i,0);
                double line=row.item.price*row.qty;
                sb.append(row.item.name).append(" x").append(row.qty)
                  .append("  RM").append(df.format(line)).append("\n");
                total+=line;
            }
            sb.append("\nTotal: RM").append(df.format(total));
            JTextArea ta=new JTextArea(sb.toString());
            ta.setEditable(false); ta.setMargin(new Insets(10,10,10,10));
            JOptionPane.showMessageDialog(f,new JScrollPane(ta),"Receipt",
                    JOptionPane.INFORMATION_MESSAGE);
            cartModel.setRowCount(0);
            cards.show(main,"choice");
        });

        /* layout */
        JPanel menuPanel=new JPanel(new BorderLayout());
        menuPanel.add(new JLabel("Available Menus"),BorderLayout.NORTH);
        menuPanel.add(menuScroll,BorderLayout.CENTER);
        menuPanel.add(addBtn,BorderLayout.SOUTH);

        JPanel cartPanel=new JPanel(new BorderLayout());
        cartPanel.add(new JLabel("Your Cart"),BorderLayout.NORTH);
        cartPanel.add(cartScroll,BorderLayout.CENTER);

        JPanel btnBar=new JPanel(new GridLayout(1,4,5,5));
        btnBar.add(removeBtn); btnBar.add(clearBtn);
        btnBar.add(cancelBtn); btnBar.add(confirmBtn);
        cartPanel.add(btnBar,BorderLayout.SOUTH);

        JPanel center=new JPanel(new GridLayout(1,2,20,0));
        center.add(menuPanel); center.add(cartPanel);
        orderPanel.add(center,BorderLayout.CENTER);

        /* ---------- wiring ---------- */
        main.add(choicePanel,"choice");
        main.add(orderPanel,"order");
        dineBtn.addActionListener(e->cards.show(main,"order"));
        takeBtn.addActionListener(e->cards.show(main,"order"));

        f.add(main);
        cards.show(main,"choice");
        f.setVisible(true);
    }
}