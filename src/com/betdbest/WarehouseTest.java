package com.betdbest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


public class WarehouseTest {

  static final String SEMICOLON = ";";
  static final String COLON = ",";
  static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
  static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.000");

  static final Float EXPERIENCE_PRICE_BY_HOUR = 0.03f;

  static enum Warehouse {
    NEW_YORK(-4), SAN_FRANCISCO(-7);

    private Integer timeZoneOffset;

    Warehouse(int timeZoneOffset) {
      this.timeZoneOffset = timeZoneOffset;
    }

    static Warehouse fromName(String input) {
      switch (input) {
      case "New York":
        return NEW_YORK;
      case "San Francisco":
        return SAN_FRANCISCO;
      }
      return null;
    }

    public String toName() {
      switch (this) {
      case NEW_YORK:
        return "New York";
      case SAN_FRANCISCO:
        return "San Francisco";
      }
      return null;
    }

    public Integer getTimeZoneOffset() {
      return timeZoneOffset;
    }
  }

  static class Stock {
    final String itemId;
    final Warehouse warehouse;
    final int stock;

    Stock(String itemId, Warehouse warehouse, int stock) {
      this.itemId = itemId;
      this.warehouse = warehouse;
      this.stock = stock;
    }

    public String getItemId() {
      return itemId;
    }

    public Warehouse getWarehouse() {
      return warehouse;
    }

    public int getStock() {
      return stock;
    }

  }

  static class BoxType {
    final String boxType;
    final int maxWeight;
    final int length, width, height; // cm
    final float volume; // dm3

    BoxType(String boxType, int maxWeight, int length, int width, int height, float volume) {
      this.boxType = boxType;
      this.maxWeight = maxWeight;
      this.length = length;
      this.width = width;
      this.height = height;
      this.volume = volume;
    }

    public String getBoxType() {
      return boxType;
    }

    public int getMaxWeight() {
      return maxWeight;
    }

    public int getLength() {
      return length;
    }

    public int getWidth() {
      return width;
    }

    public int getHeight() {
      return height;
    }

    public float getVolume() {
      return volume;
    }

  }

  static class CarrierPricing {
    final Warehouse warehouse;
    final String targetState;
    final float volumePrice; // $/dm3

    CarrierPricing(Warehouse warehouse, String targetState, float volumePrice) {
      this.warehouse = warehouse;
      this.targetState = targetState;
      this.volumePrice = volumePrice;
    }

    public Warehouse getWarehouse() {
      return warehouse;
    }

    public String getTargetState() {
      return targetState;
    }

    public float getVolumePrice() {
      return volumePrice;
    }

  }

  static class ShippingHour {
    final DayOfWeek day;
    final LocalTime time;

    ShippingHour(DayOfWeek day, LocalTime time) {
      this.time = time;
      this.day = day;
    }

    public DayOfWeek getDay() {
      return day;
    }

    public LocalTime getTime() {
      return time;
    }

  }

  static class DepartureTime {
    final Warehouse warehouse;
    final String targetState;
    final List<ShippingHour> shippingHours;

    DepartureTime(Warehouse warehouse, String targetState, List<ShippingHour> shippingHours) {
      this.warehouse = warehouse;
      this.targetState = targetState;
      this.shippingHours = shippingHours;
    }

    public Warehouse getWarehouse() {
      return warehouse;
    }

    public String getTargetState() {
      return targetState;
    }

    public List<ShippingHour> getShippingHours() {
      return shippingHours;
    }
  }

  static class CarrierTime {
    final Warehouse warehouse;
    final String targetState;
    final int carrierTime; // in hours

    CarrierTime(Warehouse warehouse, String targetState, int carrierTime) {
      this.warehouse = warehouse;
      this.targetState = targetState;
      this.carrierTime = carrierTime;
    }

    public Warehouse getWarehouse() {
      return warehouse;
    }

    public String getTargetState() {
      return targetState;
    }

    public int getCarrierTime() {
      return carrierTime;
    }

  }

  static class Item {
    final String itemId;
    final int weight;
    final int length, width, height;

    Item(String itemId, int weight, int length, int width, int height) {
      this.itemId = itemId;
      this.weight = weight;
      this.length = length;
      this.width = width;
      this.height = height;
    }

    public String getItemId() {
      return itemId;
    }

    public int getLength() {
      return length;
    }

    public int getWeight() {
      return weight;
    }

    public int getWidth() {
      return width;
    }

    public int getHeight() {
      return height;
    }

  }

  static class Order {
    final long orderId;
    final LocalDateTime orderDate;
    final String itemId;
    final String targetState;

    Order(long orderId, LocalDateTime orderDate, String itemId, String targetState) {
      this.orderId = orderId;
      this.itemId = itemId;
      this.orderDate = orderDate;
      this.targetState = targetState;
    }

    public long getOrderId() {
      return orderId;
    }

    public LocalDateTime getOrderDate() {
      return orderDate;
    }

    public String getItemId() {
      return itemId;
    }

    public String getTargetState() {
      return targetState;
    }

  }

  static class ShipmentInfo {
    final Order order;
    final Warehouse warehouse;
    final LocalDateTime guaranteedDeliveryDate;
    final String boxType;
    final float shippingPrice;

    ShipmentInfo(Order order, Warehouse warehouse, LocalDateTime guaranteedDeliveryDate, String boxType,
        float shippingPrice) {
      this.order = order;
      this.warehouse = warehouse;
      this.guaranteedDeliveryDate = guaranteedDeliveryDate;
      this.boxType = boxType;
      this.shippingPrice = shippingPrice;
    }

    public Order getOrder() {
      return order;
    }

    public String getItemId() {
      return order.itemId;
    }

    public Warehouse getWarehouse() {
      return warehouse;
    }

    public LocalDateTime getGuaranteedDeliveryDate() {
      return guaranteedDeliveryDate;
    }

    public String getBoxType() {
      return boxType;
    }

    public float getShippingPrice() {
      return shippingPrice;
    }

    public String toCsvLine() {
      return new StringBuilder().append(order.orderId).append(SEMICOLON).append(warehouse.toName()).append(SEMICOLON)
          .append(DATE_PATTERN.format(guaranteedDeliveryDate)).append(SEMICOLON).append(boxType).append(SEMICOLON)
          .append(DECIMAL_FORMAT.format(shippingPrice)).append(SEMICOLON)
          .append(DECIMAL_FORMAT.format(getShippingExperiencePrice())).toString();
    }

    public Float getShippingExperiencePrice() {
      long hours = order.orderDate.until(guaranteedDeliveryDate, ChronoUnit.HOURS);
      return hours * EXPERIENCE_PRICE_BY_HOUR;
    }

    public Float getTotalPrice() {
      return getShippingPrice() + getShippingExperiencePrice();
    }
  }

  static class CsvParser {

    public static final Order parseOrder(String inputLine) {
      String[] input = inputLine.split(SEMICOLON);
      LocalDateTime orderDate = LocalDateTime.parse(input[1], DATE_PATTERN);
      return new Order(Long.valueOf(input[0]), orderDate, input[2], input[4]);
    }

    public static final Stock parseStock(String inputLine) {
      String[] input = inputLine.split(SEMICOLON);
      return new Stock(input[0], Warehouse.fromName(input[1]), Integer.valueOf(input[2]));
    }

    public static final BoxType parseBoxType(String inputLine) {
      String[] input = inputLine.split(SEMICOLON);
      return new BoxType(input[0], Integer.valueOf(input[1]), Integer.valueOf(input[2]), Integer.valueOf(input[3]),
          Integer.valueOf(input[4]), Float.valueOf(input[5].replaceAll(",", ".")));
    }

    public static final CarrierPricing parseCarrierPricings(String inputLine) {
      String[] input = inputLine.split(SEMICOLON);
      String costString = input[2];
      return new CarrierPricing(Warehouse.fromName(input[0]), input[1], Float.valueOf(costString.replaceAll(",", ".")));
    }

    public static final DepartureTime parseDepartureTime(String inputLine) {
      String[] input = inputLine.split(SEMICOLON);
      Warehouse warehouse = Warehouse.fromName(input[0]);

      String departureTimes[] = input[2].split(COLON);
      List<ShippingHour> shippingHours = Arrays.stream(departureTimes).map(new Function<String, ShippingHour>() {

        @Override
        public ShippingHour apply(String departureTime) {
          String[] values = departureTime.trim().split(" ");
          DayOfWeek dayOfWeek = DayOfWeek.valueOf(values[0]);
          LocalTime localTime = LocalTime.parse(values[1]);
          return new ShippingHour(dayOfWeek, localTime);
        }
      }).collect(Collectors.toList());
      return new DepartureTime(warehouse, input[1], shippingHours);
    }

    public static final CarrierTime parseCarrierTime(String inputLine) {
      String[] input = inputLine.split(SEMICOLON);
      return new CarrierTime(Warehouse.fromName(input[0]), input[1], Integer.valueOf(input[2].split(" ")[0]));
    }

    public static final Item parseItem(String inputLine) {
      String[] input = inputLine.split(SEMICOLON);
      return new Item(input[0], Integer.valueOf(input[2]), Integer.valueOf(input[3]), Integer.valueOf(input[4]),
          Integer.valueOf(input[5]));
    }
  }

  static class ShipmentsManager {

    final Integer PACKAGE_PREPARATION_HOURS = 4;

    static class NoSuitableBoxException extends RuntimeException {
      private static final long serialVersionUID = 7513400494522133911L;

      public NoSuitableBoxException(String itemId) {
        super("There is no suitable for item " + itemId);
      }
    }

    static class NoSuitableWarehouseException extends RuntimeException {
      private static final long serialVersionUID = 7513400494522133911L;

      public NoSuitableWarehouseException(String itemId, String targetState) {
        super("There is no warehouse with stock and deliver options for item " + itemId + " and target state "
            + targetState);
      }
    }
    
    private List<Item> items;
    private List<BoxType> boxTypes;
    private List<Stock> stocks;
    private List<CarrierPricing> pricings;
    private List<CarrierTime> times;
    private List<DepartureTime> departures;

    public ShipmentsManager(List<Item> items, List<BoxType> boxTypes, List<CarrierPricing> carrierPricings,
        List<DepartureTime> departureTimes, List<CarrierTime> carrierTimes, List<Stock> initialStocks) {
      this.items = items;
      this.boxTypes = boxTypes;
      this.pricings = carrierPricings;
      this.departures = departureTimes;
      this.times = carrierTimes;
      this.stocks = initialStocks;
    }
    
    public ShipmentInfo findBestShipmentInfo(Order order) throws NoSuitableWarehouseException, NoSuitableBoxException {
      ArrayList<Warehouse> availableWarehouses = new ArrayList<>();

      // First of all we check if we have stock in all of our warehouses for the given
      // order
      for (Stock stock : this.stocks) {
        if ((stock.getItemId().equals(order.getItemId())) && (stock.getStock() > 0)) {
          availableWarehouses.add(stock.getWarehouse());
        }
        // Avoid extra iterations...
        if (availableWarehouses.size() == Warehouse.values().length)
          break;
      }

      if (availableWarehouses.size() == 0) {
        throw new NoSuitableWarehouseException(order.getItemId(), order.getTargetState());
      }

      BoxType boxType = findBestBoxType(order);
      ShipmentInfo info = findBestRoute(availableWarehouses, order, boxType);

      if (info == null)
        throw new NoSuitableWarehouseException(order.getItemId(), order.getTargetState());

      decreaseStock(info.getWarehouse(), order);

      return info;
    }
    
    private ShipmentInfo findBestRoute(List<Warehouse> warehouses, Order order, BoxType box)
        throws NoSuitableWarehouseException {
    	
    	//Tendria que buscar la condicion en la que sean iguales la hora de salida del almacen y la hora de recogida de los transportistas y con ello obtener el warehouse que necesito,
    	//una vez obtenido el warehouse correcto, recorreria la Lista de warehouses con un bucle for hasta encontrar en la lista el warehouse que estamos buscando, seleccionando este 
    	//warehouse como el bestwarehouse. (No he podido encontrar la relacción entre la hora de salida del almacen y la hora de recogida de los transportistas, por lo que dejare como
    	//bestwarehouse el primero de la lista.
    	Warehouse bestwarehouse = warehouses.get(0);
    	
    	//Para calcular la hora utilizamos el metodo getDeliveryDateTime, para ello necesitamos los atributos necesarios para el metodo getDeliveryDateTime que son un LocalDateTime orderDate,
    	//ShippingHour shippingHour, CarrierTime time
    	
    	LocalDateTime besttime = order.getOrderDate();
    	for (DepartureTime departure : this.departures) {
    		for (CarrierTime carrier : this.times) {
    			
    			List<ShippingHour> listashiping = departure.getShippingHours();
    			
    			for (ShippingHour i : listashiping) {
    				ShippingHour shippinghour = i;
    				CarrierTime carriertime = new CarrierTime(warehouses.get(0),order.getTargetState(),carrier.getCarrierTime());
    				besttime = getDeliveryDateTime(order.getOrderDate(),shippinghour,carriertime);
    			}
    			
    		}
    		
    	}

    	float preciototal = 0.0f;
    	//Hacemos el calculo del precio segun el volumen, ya que el precio de experiencia se suma y se tiene en cuenta
    	//una vez ya se ejecuta el metodo getTotalPrice() de ShipmentInfo
    	for (CarrierPricing carrierpricing : this.pricings) {
    		if ((carrierpricing.getTargetState().equals(order.getTargetState()))) {
    				preciototal = carrierpricing.getVolumePrice();
    	        }
    	}
    	
    	Order bestorder = order;
    	String bestbox = box.getBoxType();
    	
    	//Creamos el objeto ShipmentInfo con todos los atributos ya cargados y lo devolvemos
    	ShipmentInfo bestshipmentinfo = new ShipmentInfo(bestorder, bestwarehouse, besttime, bestbox, preciototal);
    	return bestshipmentinfo;
    }
    
    private BoxType findBestBoxType(Order order) throws NoSuitableBoxException {
    	//El item por defecto lo metemos en una caja grande para evitar que no quepa
    	int tipocaja = 5;
    	//Recorremos todos los items, buscando el item que contiene order
    	//Una vez tenemos ese item localizado, obtenemos sus valores peso, altura, ancho y largo para comparar
    	//con las diferentes cajas y elegir la mejor.
        for (Item item : this.items) {
            if ((item.getItemId().equals(order.getItemId()))) {
            	int height = item.getHeight();
    			int lenght = item.getLength();
    			int weight = item.getWeight();
    			int width = item.getWidth();
    			if ((weight <= 200) && (lenght <= 20) && (width <= 25) && (height <= 5)){
    				tipocaja = 1;
    			} else if ((weight <= 800) && (lenght <= 20) && (width <= 25) && (height <= 8)) {
    				tipocaja = 2;
    			} else if ((weight <= 1300) && (lenght <= 30) && (width <= 40) && (height <= 12)) {
    				tipocaja = 3;
    			} else if ((weight <= 1700) && (lenght <= 35) && (width <= 40) && (height <= 18)) {
    				tipocaja = 4;
    			} else if ((weight <= 2000) && (lenght <= 45) && (width <= 60) && (height <= 25)) {
    				tipocaja = 5;
    			}
    			
           }
            
       }
       
       //Una vez sabemos que tipo de caja necesitamos de entre los 5 tipos de caja, devolvemos el objeto BoxType
       //que se debe devolver en cada caso
       if (tipocaja == 1) {
    	   BoxType bestboxtype = new BoxType("XS",200,20,25,5,2.5f);
			return bestboxtype;
       }
       if (tipocaja == 2) {
    	   BoxType bestboxtype= new BoxType("S",800,20,25,8,4.0f);
			return bestboxtype;
       }
       if (tipocaja == 3) {
    	   BoxType bestboxtype = new BoxType("M",1300,30,40,12,14.4f);
			return bestboxtype;
       }
       if (tipocaja == 4) {
    	   BoxType bestboxtype = new BoxType("L",1700,35,40,18,25.2f);
			return bestboxtype;
       }
       if (tipocaja == 5) {
    	   BoxType bestboxtype = new BoxType("XL",2000,45,60,25,67.5f);
			return bestboxtype;
       }
    
      return null;
    }

    private LocalDateTime getDeliveryDateTime(LocalDateTime orderDate, ShippingHour shippingHour, CarrierTime time) {
      // From this hour we can send the order
      LocalDateTime startDate = orderDate.plusHours(PACKAGE_PREPARATION_HOURS);
      startDate = startDate.plusHours(time.getWarehouse().getTimeZoneOffset());

      // Next day we can ship the order
      LocalDateTime nextDeliveryDay = startDate.with(TemporalAdjusters.next(shippingHour.getDay()))
          .withHour(shippingHour.getTime().getHour()).withMinute(shippingHour.getTime().getMinute());

      int days = (int) startDate.until(nextDeliveryDay, ChronoUnit.DAYS);

      if (days == 7 && (nextDeliveryDay.getHour() * 60 + nextDeliveryDay.getMinute()) >= (startDate.getHour() * 60
          + startDate.getMinute())) { // We can send the day of the order
        LocalDateTime arrivalDate = startDate.withHour(shippingHour.getTime().getHour())
            .withMinute(shippingHour.getTime().getMinute()) // Today at the hour the carrier leaves
            .plusHours(time.getCarrierTime()).minusHours(time.getWarehouse().getTimeZoneOffset()); // Plus offset
        return arrivalDate;
      } else { // Send it ASAP
        return nextDeliveryDay.plusHours(time.getCarrierTime()).minusHours(time.getWarehouse().getTimeZoneOffset());
      }
    }
    
    /**
     * Overwrites our stock of the Order's item with a unit less
     */
    private void decreaseStock(Warehouse warehouse, Order order) {
      for (Stock stock : this.stocks) {
        if (stock.getWarehouse() != warehouse)
          continue;
        if (stock.getItemId().equals(order.getItemId())) {
          int s = stock.getStock();
          --s;
          this.stocks.set(this.stocks.indexOf(stock), new Stock(stock.getItemId(), warehouse, s));
          break;
        }
      }
    }
  }

  public static void main(String[] args) throws IOException {
    List<Stock> stocks = new ArrayList<>();
    List<BoxType> boxTypes = new ArrayList<>();
    List<CarrierPricing> carrierPricings = new ArrayList<>();
    List<DepartureTime> departureTimes = new ArrayList<>();
    List<CarrierTime> carrierTimes = new ArrayList<>();
    List<Item> items = new ArrayList<>();

    List<Order> orders = new ArrayList<>();

    Consumer<String> stockConsumer = input -> stocks.add(CsvParser.parseStock(input));
    Consumer<String> boxTypeConsumer = input -> boxTypes.add(CsvParser.parseBoxType(input));
    Consumer<String> carrierPricingConsumer = input -> carrierPricings.add(CsvParser.parseCarrierPricings(input));
    Consumer<String> departureTimeConsumer = input -> departureTimes.add(CsvParser.parseDepartureTime(input));
    Consumer<String> carrierTimeConsumer = input -> carrierTimes.add(CsvParser.parseCarrierTime(input));
    Consumer<String> itemConsumer = input -> items.add(CsvParser.parseItem(input));
    Consumer<String> orderConsumer = input -> orders.add(CsvParser.parseOrder(input));

    // BufferedWriter bw = new BufferedWriter(new
    // FileWriter(System.getenv("OUTPUT_PATH")));
    BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));
    // BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    BufferedReader br = new BufferedReader(new FileReader("input.txt"));

    String inputLine;
    Consumer<String> consumer = t -> {
    };
    while ((inputLine = br.readLine()) != null) {
      switch (inputLine) {
      case "---Orders---":
        consumer = orderConsumer;
        break;
      case "---Stocks---":
        consumer = stockConsumer;
        break;
      case "---BoxTypes---":
        consumer = boxTypeConsumer;
        break;
      case "---CarrierPricing---":
        consumer = carrierPricingConsumer;
        break;
      case "---DepartureTimes---":
        consumer = departureTimeConsumer;
        break;
      case "---CarrierTimes---":
        consumer = carrierTimeConsumer;
        break;
      case "---Items---":
        consumer = itemConsumer;
        break;
      default:
        consumer.accept(inputLine);
        break;
      }
    }
    br.close();
    
    Collections.sort(orders, new Comparator<Order>() {
      @Override
      public int compare(Order arg0, Order arg1) {
        return arg0.getOrderDate().compareTo(arg1.getOrderDate());
      }
    });

    ShipmentsManager shipmentsManager = new ShipmentsManager(items, boxTypes, carrierPricings, departureTimes,
        carrierTimes, stocks);

    List<ShipmentInfo> shipmentInfos = orders.stream().map(shipmentsManager::findBestShipmentInfo)
        .collect(Collectors.toList());

    Collections.sort(shipmentInfos, new Comparator<ShipmentInfo>() {
      @Override
      public int compare(ShipmentInfo arg0, ShipmentInfo arg1) {
        return arg0.getOrder().getOrderDate().compareTo(arg1.getOrder().getOrderDate());
      }
    });

    Float totalShipmentPrice = 0.0f;

    for (ShipmentInfo shipmentInfo : shipmentInfos) {
      totalShipmentPrice += shipmentInfo.shippingPrice + shipmentInfo.getShippingExperiencePrice();
    }
    StringBuilder output = new StringBuilder();
    output.append(totalShipmentPrice + "\n");
    for (ShipmentInfo shipmentInfo : shipmentInfos) {
      output.append(shipmentInfo.toCsvLine() + "\n");
    }
    bw.write(output.toString());
    bw.close();
    System.out.println("Your total shipment price is: " + totalShipmentPrice);
  };

}
