import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashMap;

public class ModernShoppingCart extends JFrame {

    JLabel lblTotal;
    JLabel cartIcon;

    int total = 0; 

    LinkedHashMap<String, Integer> cartQty = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> priceMap = new LinkedHashMap<>();

    JPanel cartItemsPanel;

    // ================= MEMBER CLASS =================
    class Validator {
        boolean isEmptyCart() {
            return cartQty.isEmpty();
        }

        boolean isValidName(String name) {
            return name != null && !name.trim().isEmpty();
        }
    }

    // ================= CONSTRUCTOR =================
    public ModernShoppingCart() {

        setTitle("Auréa Perfume");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gp = new GradientPaint(0, 0,new Color(255, 105, 180),
                        getWidth(), 0,new Color(70, 130, 180)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        header.setPreferredSize(new Dimension(1200, 140));

        JLabel logo = new JLabel();
        ImageIcon logoIcon = new ImageIcon("logoo.png");
        Image logoImg = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(logoImg));

        JLabel title = new JLabel("Auréa Perfume");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));

        left.add(logo);
        left.add(Box.createHorizontalStrut(12));
        left.add(title);

        header.add(left, BorderLayout.WEST);

        cartIcon = new JLabel("🛒 0");
        cartIcon.setFont(new Font("Arial", Font.BOLD, 22));
        cartIcon.setForeground(Color.WHITE);
        cartIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        header.add(cartIcon, BorderLayout.EAST);

        main.add(header, BorderLayout.NORTH);

        // ================= PRODUCTS =================
        JPanel productsPanel = new JPanel(new GridLayout(2, 3, 25, 25));
        productsPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        productsPanel.setBackground(new Color(245, 240, 255));

        productsPanel.add(createProduct("Yara Perfume", 109, "Yara.png"));
        productsPanel.add(createProduct("Channel Perfume", 569, "ChannelP.png"));
        productsPanel.add(createProduct("Dior Blooming Bouquet", 140, "dior.png"));
        productsPanel.add(createProduct("Billie Eilish", 345, "billie.png"));
        productsPanel.add(createProduct("Beauboss : Not your Ex", 45, "beauboss.png"));
        productsPanel.add(createProduct("Rabanne Phantom Perfume", 530, "phantom.png"));

        JScrollPane productScroll = new JScrollPane(productsPanel);

        // ================= CART =================
        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        cartPanel.setPreferredSize(new Dimension(320, 0));
        cartPanel.setBackground(new Color(230, 240, 255));

        JLabel cartTitle = new JLabel("My Cart");
        cartTitle.setFont(new Font("Arial", Font.BOLD, 18));
        cartTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));

        JScrollPane cartScroll = new JScrollPane(cartItemsPanel);

        lblTotal = new JLabel("Total: RM0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotal.setForeground(new Color(255, 105, 180));

        JButton checkout = new JButton("Checkout");

        // ================= ANONYMOUS CLASS=================
        checkout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPaymentDialog();
            }
        });

        checkout.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkout.setBackground(new Color(255, 105, 180));
        checkout.setForeground(Color.WHITE);

        cartPanel.add(Box.createVerticalStrut(10));
        cartPanel.add(cartTitle);
        cartPanel.add(cartScroll);
        cartPanel.add(lblTotal);
        cartPanel.add(Box.createVerticalStrut(10));
        cartPanel.add(checkout);

        main.add(productScroll, BorderLayout.CENTER);
        main.add(cartPanel, BorderLayout.EAST);

        add(main);
    }

    // ================= PRODUCT CARD =================
    public JPanel createProduct(String name, int price, String imagePath) {

        JPanel card = new JPanel(new BorderLayout());

        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(230, 230, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel(new ImageIcon(img));

        JLabel nameLabel = new JLabel(name, JLabel.CENTER);
        JLabel priceLabel = new JLabel("RM " + price, JLabel.CENTER);

        JButton add = new JButton("Add to Cart");

        add.addActionListener(e -> {
            cartQty.put(name, cartQty.getOrDefault(name, 0) + 1);
            priceMap.put(name, price);
            total += price;
            refreshCart();
        });

        JPanel center = new JPanel(new GridLayout(2, 1));
        center.add(nameLabel);
        center.add(priceLabel);

        card.add(imgLabel, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);
        card.add(add, BorderLayout.SOUTH);

        return card;
    }

    // ================= CART  =================
    public void refreshCart() {

        cartItemsPanel.removeAll();

        int totalQty = 0;

        for (String name : cartQty.keySet()) {

            int qty = cartQty.get(name);
            int price = priceMap.get(name);

            totalQty += qty;

            JPanel item = new JPanel(new BorderLayout());
            item.setBackground(Color.WHITE);

            JLabel text = new JLabel(name + " x" + qty + " - RM" + (qty * price));

            JButton delete = new JButton("🗑");

            delete.addActionListener(e -> {
                cartQty.put(name, qty - 1);
                total -= price;

                if (cartQty.get(name) <= 0) {
                    cartQty.remove(name);
                    priceMap.remove(name);
                }

                refreshCart();
            });

            item.add(text, BorderLayout.CENTER);
            item.add(delete, BorderLayout.EAST);

            cartItemsPanel.add(item);
        }

        lblTotal.setText("Total: RM" + total);
        cartIcon.setText("🛒 " + totalQty);

        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    // ================= CHECKOUT =================
    public void showPaymentDialog() {

        Validator validator = new Validator();

        // ================= LOCAL CLASS =================
        class ShippingChecker {
            int getShipping(int index) {
                return (index == 0) ? 10 : 20;
            }
        }

        ShippingChecker sc = new ShippingChecker();

        if (validator.isEmptyCart()) {
            JOptionPane.showMessageDialog(this,
                    "Please choose product before checkout!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();

        JCheckBox giftWrap = new JCheckBox("Gift Wrap (+RM5)");

        JRadioButton online = new JRadioButton("Online Banking");
        JRadioButton card = new JRadioButton("Card");
        JRadioButton tng = new JRadioButton("TNG eWallet");
        JRadioButton atome = new JRadioButton("Atome");

        ButtonGroup group = new ButtonGroup();
        group.add(online);
        group.add(card);
        group.add(tng);
        group.add(atome);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Address:"));
        panel.add(addressField);

        panel.add(new JLabel("Shipping: Semenanjung / Sabah Sarawak"));
        String[] shipping = {"Semenanjung", "Sabah & Sarawak"};
        JComboBox<String> shipBox = new JComboBox<>(shipping);
        panel.add(shipBox);

        panel.add(new JLabel("Payment Method:"));
        panel.add(online);
        panel.add(card);
        panel.add(tng);
        panel.add(atome);

        panel.add(giftWrap);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Checkout", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {

            int shippingFee = sc.getShipping(shipBox.getSelectedIndex());
            int gift = giftWrap.isSelected() ? 5 : 0;

            int finalTotal = total + shippingFee + gift;

            String method = online.isSelected() ? "Online Banking"
                    : card.isSelected() ? "Card"
                    : tng.isSelected() ? "TNG eWallet"
                    : "Atome";

            // ================= SHADOW VARIABLE=================
            class Receipt {
                int total = finalTotal; 

                String generate() {
                    return "TOTAL : RM" + total;
                }
            }

            Receipt r = new Receipt();

            StringBuilder receipt = new StringBuilder();
            receipt.append("===== AUREA PERFUME RECEIPT =====\n\n");

            for (String item : cartQty.keySet()) {
                int qty = cartQty.get(item);
                int price = priceMap.get(item);

                receipt.append(item)
                        .append(" x").append(qty)
                        .append(" = RM").append(qty * price)
                        .append("\n");
            }

            receipt.append("\nShipping: RM").append(shippingFee);
            receipt.append("\nPayment: ").append(method);
            receipt.append("\nGift Wrap: RM").append(gift);
            receipt.append("\n").append(r.generate());

            JOptionPane.showMessageDialog(this, receipt.toString());

            cartQty.clear();
            priceMap.clear();
            total = 0;
            refreshCart();
        }
    }

    public static void main(String[] args) {
        new ModernShoppingCart().setVisible(true);
    }
}