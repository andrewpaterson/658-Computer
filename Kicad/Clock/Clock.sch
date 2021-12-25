EESchema Schematic File Version 4
EELAYER 30 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 1 1
Title ""
Date ""
Rev ""
Comp ""
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
$Comp
L power:+3.3V #PWR01
U 1 1 61C48A03
P 1050 850
F 0 "#PWR01" H 1050 700 50  0001 C CNN
F 1 "+3.3V" H 1065 1023 50  0000 C CNN
F 2 "" H 1050 850 50  0001 C CNN
F 3 "" H 1050 850 50  0001 C CNN
	1    1050 850 
	1    0    0    -1  
$EndComp
$Comp
L power:GNDREF #PWR02
U 1 1 61C49261
P 1050 950
F 0 "#PWR02" H 1050 700 50  0001 C CNN
F 1 "GNDREF" H 1055 777 50  0000 C CNN
F 2 "" H 1050 950 50  0001 C CNN
F 3 "" H 1050 950 50  0001 C CNN
	1    1050 950 
	1    0    0    -1  
$EndComp
Wire Wire Line
	4250 1900 4150 1900
Wire Wire Line
	4250 1900 4250 2000
Wire Wire Line
	4250 2000 4150 2000
Wire Wire Line
	4150 2100 4250 2100
Wire Wire Line
	4250 2100 4250 2200
Wire Wire Line
	4250 2200 4150 2200
Wire Wire Line
	4150 2300 4250 2300
Wire Wire Line
	4250 2300 4250 2400
Wire Wire Line
	4250 2400 4150 2400
Wire Wire Line
	1900 2700 1900 2750
Wire Wire Line
	3150 2000 3050 2000
Wire Wire Line
	3050 2000 3050 2100
Wire Wire Line
	3050 2100 3150 2100
Wire Wire Line
	3150 2200 3050 2200
Wire Wire Line
	3050 2200 3050 2300
Wire Wire Line
	3050 2300 3150 2300
Wire Wire Line
	1900 1300 1900 2100
Wire Wire Line
	1150 2750 1300 2750
Wire Wire Line
	6800 1850 6900 1850
Wire Wire Line
	6900 1850 6900 1400
Wire Wire Line
	6900 1850 6900 1950
Wire Wire Line
	6900 1950 6800 1950
Connection ~ 6900 1850
Wire Wire Line
	6900 2250 6800 2250
Connection ~ 6900 1950
Wire Wire Line
	6800 2350 7200 2350
Wire Wire Line
	7200 2650 5600 2650
Wire Wire Line
	5600 2650 5600 2050
Wire Wire Line
	5600 2050 5700 2050
Wire Wire Line
	5700 2350 5500 2350
Wire Wire Line
	5500 2350 5500 1950
Wire Wire Line
	5500 1950 5700 1950
Wire Wire Line
	5700 1850 5400 1850
Wire Wire Line
	5400 1850 5400 1400
Wire Wire Line
	5400 1850 5400 2150
Wire Wire Line
	5400 2150 5700 2150
Connection ~ 5400 1850
$Comp
L Device:CP C4
U 1 1 61C8A940
P 2400 800
F 0 "C4" H 2518 846 50  0000 L CNN
F 1 "CP1000uF" H 2200 550 50  0000 L CNN
F 2 "Capacitor_THT:CP_Radial_D7.5mm_P2.50mm" H 2438 650 50  0001 C CNN
F 3 "~" H 2400 800 50  0001 C CNN
	1    2400 800 
	1    0    0    -1  
$EndComp
$Comp
L Connector:Conn_01x02_Male J1
U 1 1 61C9B9E9
P 4600 2250
F 0 "J1" H 4708 2431 50  0000 C CNN
F 1 "Conn_01x02_Male" H 4708 2340 50  0000 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x02_P2.54mm_Vertical" H 4600 2250 50  0001 C CNN
F 3 "~" H 4600 2250 50  0001 C CNN
	1    4600 2250
	1    0    0    -1  
$EndComp
Wire Wire Line
	4800 2250 5700 2250
Wire Wire Line
	5500 2350 4800 2350
Connection ~ 5500 2350
Wire Wire Line
	1500 2400 1400 2400
Wire Wire Line
	1400 2400 1400 1300
$Comp
L power:+5V #PWR0101
U 1 1 61C5CAF2
P 1250 750
F 0 "#PWR0101" H 1250 600 50  0001 C CNN
F 1 "+5V" H 1265 923 50  0000 C CNN
F 2 "" H 1250 750 50  0001 C CNN
F 3 "" H 1250 750 50  0001 C CNN
	1    1250 750 
	1    0    0    -1  
$EndComp
$Comp
L Connector_Generic:Conn_02x03_Odd_Even J3
U 1 1 61C5DE69
P 1800 850
F 0 "J3" H 1850 1167 50  0000 C CNN
F 1 "Conn_02x03" H 1850 600 50  0000 C CNN
F 2 "Connector_PinSocket_2.54mm:PinSocket_2x03_P2.54mm_Vertical" H 1800 850 50  0001 C CNN
F 3 "~" H 1800 850 50  0001 C CNN
	1    1800 850 
	-1   0    0    1   
$EndComp
$Comp
L Device:CP C1
U 1 1 61C5F212
P 2900 800
F 0 "C1" H 3018 846 50  0000 L CNN
F 1 "CP1000uF" H 2700 550 50  0000 L CNN
F 2 "Capacitor_THT:CP_Radial_D7.5mm_P2.50mm" H 2938 650 50  0001 C CNN
F 3 "~" H 2900 800 50  0001 C CNN
	1    2900 800 
	1    0    0    -1  
$EndComp
Wire Wire Line
	2000 950  2100 950 
Wire Wire Line
	2400 650  2250 650 
Wire Wire Line
	2250 650  2250 850 
Wire Wire Line
	2250 850  2000 850 
Wire Wire Line
	2000 750  2150 750 
Wire Wire Line
	2150 750  2150 550 
Wire Wire Line
	2900 550  2900 650 
Wire Wire Line
	2400 650  2650 650 
Connection ~ 2400 650 
Wire Wire Line
	2150 550  2900 550 
Wire Wire Line
	2400 950  2900 950 
Connection ~ 2400 950 
Wire Wire Line
	2650 1300 2550 1300
Wire Wire Line
	2650 650  2650 1300
Connection ~ 2650 1300
Wire Wire Line
	2100 950  2100 1400
Wire Wire Line
	2100 1400 1300 1400
Connection ~ 2100 950 
Wire Wire Line
	2100 950  2400 950 
Connection ~ 2100 1400
$Comp
L SamacSys_Parts:885012207027 C0.1nF_0
U 1 1 61CEFC2B
P 1150 1800
F 0 "C0.1nF_0" V 1400 1600 50  0000 L CNN
F 1 "885012207027" V 1500 1200 50  0001 L CNN
F 2 "CAPC2012X90N" H 1500 1850 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207027.pdf" H 1500 1750 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 100pF X7R0805101K016DFCT10000" H 1500 1650 50  0001 L CNN "Description"
F 5 "0.9" H 1500 1550 50  0001 L CNN "Height"
F 6 "710-885012207027" H 1500 1450 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207027?qs=0KOYDY2FL28IWYVDh0DkrQ%3D%3D" H 1500 1350 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 1500 1250 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207027" H 1500 1150 50  0001 L CNN "Manufacturer_Part_Number"
	1    1150 1800
	0    1    1    0   
$EndComp
Wire Wire Line
	2100 1400 2950 1400
Wire Wire Line
	1150 2300 1150 2750
Wire Wire Line
	1300 1400 1300 2750
Connection ~ 1300 2750
Wire Wire Line
	1300 2750 1900 2750
NoConn ~ 4150 2500
Wire Wire Line
	2950 2500 2950 1400
Connection ~ 2950 2500
Wire Wire Line
	2950 2500 3150 2500
$Comp
L Wurth_Elektronik:885012207027 C0.1nF_1
U 1 1 61C612DC
P 2800 1300
F 0 "C0.1nF_1" V 3050 1200 50  0000 L CNN
F 1 "885012207027" V 3150 700 50  0001 L CNN
F 2 "CAPC2012X90N" H 3150 1350 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207027.pdf" H 3150 1250 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 100pF X7R0805101K016DFCT10000" H 3150 1150 50  0001 L CNN "Description"
F 5 "0.9" H 3150 1050 50  0001 L CNN "Height"
F 6 "710-885012207027" H 3150 950 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207027?qs=0KOYDY2FL28IWYVDh0DkrQ%3D%3D" H 3150 850 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 3150 750 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207027" H 3150 650 50  0001 L CNN "Manufacturer_Part_Number"
	1    2800 1300
	0    1    1    0   
$EndComp
Wire Wire Line
	2800 1800 2800 2500
Wire Wire Line
	2800 2500 2950 2500
Wire Wire Line
	4250 1900 4250 1300
Connection ~ 4250 1900
Wire Wire Line
	2650 1300 2800 1300
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_1
U 1 1 61C6AFA3
P 2550 1300
F 0 "C1.0nF_1" V 2800 1100 50  0000 L CNN
F 1 "885012207033" H 2800 1474 50  0001 C CNN
F 2 "CAPC2012X90N" H 2900 1350 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 2900 1250 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 2900 1150 50  0001 L CNN "Description"
F 5 "0.9" H 2900 1050 50  0001 L CNN "Height"
F 6 "710-885012207033" H 2900 950 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 2900 850 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 2900 750 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 2900 650 50  0001 L CNN "Manufacturer_Part_Number"
	1    2550 1300
	0    1    1    0   
$EndComp
Wire Wire Line
	2800 2500 2550 2500
Wire Wire Line
	2550 2500 2550 1800
Connection ~ 2800 2500
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_2
U 1 1 61C74217
P 5150 1300
F 0 "C1.0nF_2" V 5400 1200 50  0000 L CNN
F 1 "885012207033" H 5400 1474 50  0001 C CNN
F 2 "CAPC2012X90N" H 5500 1350 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 5500 1250 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 5500 1150 50  0001 L CNN "Description"
F 5 "0.9" H 5500 1050 50  0001 L CNN "Height"
F 6 "710-885012207033" H 5500 950 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 5500 850 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 5500 750 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 5500 650 50  0001 L CNN "Manufacturer_Part_Number"
	1    5150 1300
	0    1    1    0   
$EndComp
Wire Wire Line
	5300 2450 5300 1300
Wire Wire Line
	5150 2450 5300 2450
Connection ~ 5300 2450
Wire Wire Line
	5150 1800 5150 2450
Wire Wire Line
	3150 2400 3050 2400
Wire Wire Line
	3050 2400 3050 2750
$Comp
L Texas_Instruments:SN74F20DR F20
U 1 1 61C6185B
P 4400 3700
F 0 "F20" H 4950 3400 50  0000 C CNN
F 1 "SN74F20DR" H 4950 2950 50  0000 C CNN
F 2 "SOIC127P600X175-14N" H 5350 3800 50  0001 L CNN
F 3 "https://www.ti.com/lit/gpn/sn74f20" H 5350 3700 50  0001 L CNN
F 4 "Dual 4-input positive-NAND gates" H 5350 3600 50  0001 L CNN "Description"
F 5 "1.75" H 5350 3500 50  0001 L CNN "Height"
F 6 "595-SN74F20DR" H 5350 3400 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Texas-Instruments/SN74F20DR?qs=mE33ZKBHyE7dV637H3Qthw%3D%3D" H 5350 3300 50  0001 L CNN "Mouser Price/Stock"
F 8 "Texas Instruments" H 5350 3200 50  0001 L CNN "Manufacturer_Name"
F 9 "SN74F20DR" H 5350 3100 50  0001 L CNN "Manufacturer_Part_Number"
	1    4400 3700
	1    0    0    -1  
$EndComp
$Comp
L Nexperia:74LVC138APW-Q100J LVC138
U 1 1 61C62B44
P 4350 6200
F 0 "LVC138" H 4850 5850 50  0000 C CNN
F 1 "74LVC138APW-Q100J" H 4850 5350 50  0000 C CNN
F 2 "SOP65P640X110-16N" H 5200 6300 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC138A_Q100.pdf" H 5200 6200 50  0001 L CNN
F 4 "74LVC138A-Q100 - 3-to-8 line decoder/demultiplexer; inverting@en-us" H 5200 6100 50  0001 L CNN "Description"
F 5 "1.1" H 5200 6000 50  0001 L CNN "Height"
F 6 "771-74LVC138APWQ100J" H 5200 5900 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC138APW-Q100J?qs=fi7yB2oewZnXKE82xo%252BhJQ%3D%3D" H 5200 5800 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 5200 5700 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC138APW-Q100J" H 5200 5600 50  0001 L CNN "Manufacturer_Part_Number"
	1    4350 6200
	1    0    0    -1  
$EndComp
$Comp
L Nexperia:74LVC163PW,118 LVC163
U 1 1 61C67470
P 1650 6000
F 0 "LVC163" H 2150 5650 50  0000 C CNN
F 1 "74LVC163PW,118" H 2150 5150 50  0000 C CNN
F 2 "SOP65P640X110-16N" H 2500 6100 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC163.pdf" H 2500 6000 50  0001 L CNN
F 4 "74LVC163 - Presettable synchronous 4-bit binary counter; synchronous reset@en-us" H 2500 5900 50  0001 L CNN "Description"
F 5 "1.1" H 2500 5800 50  0001 L CNN "Height"
F 6 "771-74LVC163PW-T" H 2500 5700 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC163PW118?qs=me8TqzrmIYUWRf9xrCNBjw%3D%3D" H 2500 5600 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 2500 5500 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC163PW,118" H 2500 5400 50  0001 L CNN "Manufacturer_Part_Number"
	1    1650 6000
	1    0    0    -1  
$EndComp
$Comp
L Nexperia:74LVC574AD,118 LVC574_1
U 1 1 61C69B8F
P 8800 5200
F 0 "LVC574_1" H 9300 4750 50  0000 C CNN
F 1 "74LVC574AD,118" H 9300 4150 50  0000 C CNN
F 2 "SOIC127P1032X265-20N" H 9650 5300 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC574A.pdf" H 9650 5200 50  0001 L CNN
F 4 "74LVC574A - Octal D-type flip-flop with 5 V tolerant inputs/outputs; positive edge-trigger (3-state)@en-us" H 9650 5100 50  0001 L CNN "Description"
F 5 "2.65" H 9650 5000 50  0001 L CNN "Height"
F 6 "771-74LVC574AD-T" H 9650 4900 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC574AD118?qs=me8TqzrmIYVVWemAtQ0oag%3D%3D" H 9650 4800 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 9650 4700 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC574AD,118" H 9650 4600 50  0001 L CNN "Manufacturer_Part_Number"
	1    8800 5200
	1    0    0    -1  
$EndComp
Wire Wire Line
	2650 6200 3750 6200
Wire Wire Line
	2650 6300 3850 6300
Wire Wire Line
	2650 6400 3950 6400
Wire Wire Line
	2650 6500 4150 6500
$Comp
L Nexperia:74LVC574AD,118 LVC574_2
U 1 1 61C8B160
P 6900 5200
F 0 "LVC574_2" H 7400 4750 50  0000 C CNN
F 1 "74LVC574AD,118" H 7400 4150 50  0000 C CNN
F 2 "SOIC127P1032X265-20N" H 7750 5300 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC574A.pdf" H 7750 5200 50  0001 L CNN
F 4 "74LVC574A - Octal D-type flip-flop with 5 V tolerant inputs/outputs; positive edge-trigger (3-state)@en-us" H 7750 5100 50  0001 L CNN "Description"
F 5 "2.65" H 7750 5000 50  0001 L CNN "Height"
F 6 "771-74LVC574AD-T" H 7750 4900 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC574AD118?qs=me8TqzrmIYVVWemAtQ0oag%3D%3D" H 7750 4800 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 7750 4700 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC574AD,118" H 7750 4600 50  0001 L CNN "Manufacturer_Part_Number"
	1    6900 5200
	1    0    0    -1  
$EndComp
$Comp
L Nexperia:74LVC74APW,118 LVC74_0
U 1 1 61C7BBBF
P 5700 1850
F 0 "LVC74_0" H 6250 1550 50  0000 C CNN
F 1 "74LVC74APW,118" H 6250 1100 50  0000 C CNN
F 2 "SOP65P640X110-14N" H 6650 1950 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC74A.pdf" H 6650 1850 50  0001 L CNN
F 4 "74LVC74A - Dual D-type flip-flop with set and reset; positive-edge trigger@en-us" H 6650 1750 50  0001 L CNN "Description"
F 5 "1.1" H 6650 1650 50  0001 L CNN "Height"
F 6 "771-74LVC74APW-T" H 6650 1550 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC74APW118?qs=me8TqzrmIYVtXwVfet0lzw%3D%3D" H 6650 1450 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 6650 1350 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC74APW,118" H 6650 1250 50  0001 L CNN "Manufacturer_Part_Number"
	1    5700 1850
	1    0    0    -1  
$EndComp
$Comp
L Nexperia:74LVCU04APW,118 LVCU04
U 1 1 61C7F1FB
P 3150 1900
F 0 "LVCU04" H 3650 1600 50  0000 C CNN
F 1 "74LVCU04APW,118" H 3650 1150 50  0000 C CNN
F 2 "SOP65P640X110-14N" H 4000 2000 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVCU04A.pdf" H 4000 1900 50  0001 L CNN
F 4 "74LVCU04A - Hex unbuffered inverter@en-us" H 4000 1800 50  0001 L CNN "Description"
F 5 "1.1" H 4000 1700 50  0001 L CNN "Height"
F 6 "771-LVCU04APW118" H 4000 1600 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVCU04APW118?qs=me8TqzrmIYXibrDyFt%252BDyw%3D%3D" H 4000 1500 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 4000 1400 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVCU04APW,118" H 4000 1300 50  0001 L CNN "Manufacturer_Part_Number"
	1    3150 1900
	1    0    0    -1  
$EndComp
Wire Wire Line
	4400 4850 3750 4850
Wire Wire Line
	3750 4850 3750 6200
Connection ~ 3750 6200
Wire Wire Line
	3750 6200 4350 6200
Wire Wire Line
	4400 5050 3850 5050
Wire Wire Line
	3850 5050 3850 6300
Connection ~ 3850 6300
Wire Wire Line
	3850 6300 4350 6300
Wire Wire Line
	4400 5250 4150 5250
Wire Wire Line
	4150 5250 4150 6500
Connection ~ 4150 6500
Wire Wire Line
	4150 6500 4350 6500
Wire Wire Line
	3950 6400 3950 4000
Wire Wire Line
	3950 4000 4400 4000
Connection ~ 3950 6400
Wire Wire Line
	3950 6400 4350 6400
Wire Wire Line
	4400 5350 4050 5350
Wire Wire Line
	4050 5350 4050 4100
Wire Wire Line
	4050 4100 4400 4100
Wire Wire Line
	4400 5150 4150 5150
Wire Wire Line
	4150 5150 4150 3800
Wire Wire Line
	4150 3800 4400 3800
Wire Wire Line
	4400 3700 4250 3700
Wire Wire Line
	4250 3700 4250 4950
Wire Wire Line
	4250 4950 4400 4950
$Comp
L Maxim:DS1813R-10+T&R DS1813
U 1 1 61CEDE4E
P 8550 2000
F 0 "DS1813" H 9150 1900 50  0000 C CNN
F 1 "DS1813R-10+T&R" H 9100 2150 50  0000 C CNN
F 2 "SOT95P237X112-3N" H 9500 2100 50  0001 L CNN
F 3 "https://componentsearchengine.com/Datasheets/2/DS1813R-10+T&R.pdf" H 9500 2000 50  0001 L CNN
F 4 "Maxim DS1813R-10+T&R, Voltage Supervisor 4.25  4.49 V, Reset Input, 3-Pin SOT-23" H 9500 1900 50  0001 L CNN "Description"
F 5 "1.12" H 9500 1800 50  0001 L CNN "Height"
F 6 "700-DS1813R-10T&R" H 9500 1700 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Maxim-Integrated/DS1813R-10%2bTR?qs=0Y9aZN%252BMVCUEX%2FgvPX%252B6Nw%3D%3D" H 9500 1600 50  0001 L CNN "Mouser Price/Stock"
F 8 "Maxim Integrated" H 9500 1500 50  0001 L CNN "Manufacturer_Name"
F 9 "DS1813R-10+T&R" H 9500 1400 50  0001 L CNN "Manufacturer_Part_Number"
	1    8550 2000
	-1   0    0    1   
$EndComp
$Comp
L Nexperia:74LVC74APW,118 LVC74_1
U 1 1 61CF938B
P 9600 2000
F 0 "LVC74_1" H 10150 1700 50  0000 C CNN
F 1 "74LVC74APW,118" H 10150 1250 50  0000 C CNN
F 2 "SOP65P640X110-14N" H 10550 2100 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC74A.pdf" H 10550 2000 50  0001 L CNN
F 4 "74LVC74A - Dual D-type flip-flop with set and reset; positive-edge trigger@en-us" H 10550 1900 50  0001 L CNN "Description"
F 5 "1.1" H 10550 1800 50  0001 L CNN "Height"
F 6 "771-74LVC74APW-T" H 10550 1700 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC74APW118?qs=me8TqzrmIYVtXwVfet0lzw%3D%3D" H 10550 1600 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 10550 1500 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC74APW,118" H 10550 1400 50  0001 L CNN "Manufacturer_Part_Number"
	1    9600 2000
	1    0    0    -1  
$EndComp
Wire Wire Line
	9600 2400 9400 2400
Wire Wire Line
	9400 2400 9400 2700
Wire Wire Line
	9400 2800 11000 2800
Wire Wire Line
	11000 2800 11000 2200
Wire Wire Line
	11000 2200 10700 2200
Connection ~ 7200 2350
Wire Wire Line
	7200 2350 7200 2650
Wire Wire Line
	6900 1950 6900 2250
$Comp
L Connector:Conn_01x02_Male J2
U 1 1 61CA7E7B
P 7750 2450
F 0 "J2" H 7650 2400 50  0000 C CNN
F 1 "Conn_01x02_Male" H 7858 2540 50  0000 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x02_P2.54mm_Vertical" H 7750 2450 50  0001 C CNN
F 3 "~" H 7750 2450 50  0001 C CNN
	1    7750 2450
	-1   0    0    1   
$EndComp
Wire Wire Line
	6800 2150 7450 2150
Wire Wire Line
	7450 2750 7450 2150
Wire Wire Line
	7350 2450 7550 2450
Wire Wire Line
	6800 2450 7350 2450
Connection ~ 7350 2450
Wire Wire Line
	6800 2050 7350 2050
NoConn ~ 9600 2500
NoConn ~ 10700 2600
Connection ~ 1300 1400
Wire Wire Line
	1300 1400 550  1400
Connection ~ 1400 1300
Connection ~ 1900 1300
Wire Wire Line
	1900 1300 1400 1300
Connection ~ 2550 1300
Wire Wire Line
	2550 1300 1900 1300
Connection ~ 2800 1300
Connection ~ 5150 1300
Wire Wire Line
	5150 1300 5300 1300
Connection ~ 5300 1300
Connection ~ 5400 1400
Wire Wire Line
	5400 1400 6900 1400
Connection ~ 6900 1400
Wire Wire Line
	2800 1300 4250 1300
Wire Wire Line
	7450 2000 7450 1400
Wire Wire Line
	6900 1400 7450 1400
Connection ~ 7450 1400
Wire Wire Line
	8550 1900 8550 1200
Connection ~ 8550 1200
Wire Wire Line
	7200 2350 7550 2350
Wire Wire Line
	6000 2900 7200 2900
Wire Wire Line
	11100 2900 11100 2300
Wire Wire Line
	10700 2300 11100 2300
Wire Wire Line
	9600 2000 9500 2000
Wire Wire Line
	9600 2100 9500 2100
Wire Wire Line
	9500 2100 9500 2000
Connection ~ 9500 2000
Wire Wire Line
	10700 2000 10900 2000
Wire Wire Line
	10900 2000 10900 1650
Wire Wire Line
	9500 2000 9500 1800
Wire Wire Line
	9500 1800 10800 1800
Wire Wire Line
	10800 1800 10800 2100
Wire Wire Line
	10800 2100 10700 2100
Wire Wire Line
	10700 2400 10900 2400
Wire Wire Line
	10900 2400 10900 2000
Connection ~ 10900 2000
Connection ~ 10900 1300
Wire Wire Line
	10900 1300 11150 1300
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_9
U 1 1 61E7D541
P 10400 1650
F 0 "C1.0nF_9" H 10500 1500 50  0000 L CNN
F 1 "885012207033" H 10650 1824 50  0001 C CNN
F 2 "CAPC2012X90N" H 10750 1700 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 10750 1600 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 10750 1500 50  0001 L CNN "Description"
F 5 "0.9" H 10750 1400 50  0001 L CNN "Height"
F 6 "710-885012207033" H 10750 1300 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 10750 1200 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 10750 1100 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 10750 1000 50  0001 L CNN "Manufacturer_Part_Number"
	1    10400 1650
	-1   0    0    1   
$EndComp
Wire Wire Line
	7350 2050 7350 2200
Wire Wire Line
	8550 2000 8750 2000
Wire Wire Line
	7350 2200 9600 2200
Wire Wire Line
	5300 2450 5700 2450
Connection ~ 7350 2200
Wire Wire Line
	7350 2200 7350 2450
Connection ~ 7200 2650
Wire Wire Line
	7200 2650 7200 2900
Wire Wire Line
	9600 2300 9400 2300
Wire Wire Line
	9400 2300 9400 1300
Connection ~ 9400 1300
Wire Wire Line
	9600 2600 9250 2600
Wire Wire Line
	9250 2600 9250 1650
Connection ~ 9250 1400
Wire Wire Line
	9250 1400 11150 1400
Text Label 6200 1200 0    50   ~ 0
5V
Text Label 6200 1300 0    50   ~ 0
3.3V
Text Label 6200 1400 0    50   ~ 0
GND
Text Label 6850 2350 0    50   ~ 0
Clock
Text Label 6850 2450 0    50   ~ 0
Clock_B
Wire Wire Line
	10700 2500 10900 2500
Text Label 10750 2500 0    50   ~ 0
Reset_1
Text Label 9550 2400 2    50   ~ 0
Reset_0
Wire Wire Line
	10900 3000 8650 3000
Wire Wire Line
	10900 2500 10900 3000
Wire Wire Line
	4350 6700 2750 6700
Wire Wire Line
	2450 4700 2750 4700
Wire Wire Line
	2750 4700 2750 3000
Connection ~ 2750 4700
Wire Wire Line
	2750 4700 2750 6700
Wire Wire Line
	2450 4600 2550 4600
Wire Wire Line
	2550 5550 1550 5550
Wire Wire Line
	1550 5550 1550 6000
Wire Wire Line
	1550 6000 1650 6000
Wire Wire Line
	6000 2900 6000 5650
Wire Wire Line
	6000 5650 1450 5650
Wire Wire Line
	1450 5650 1450 6100
Wire Wire Line
	1450 6100 1650 6100
Wire Wire Line
	2650 6600 2650 6700
Wire Wire Line
	1650 6200 1650 6300
Connection ~ 1650 6300
Wire Wire Line
	1650 6300 1650 6400
Connection ~ 1650 6400
Wire Wire Line
	1650 6400 1650 6500
Wire Wire Line
	1650 6600 1550 6600
Wire Wire Line
	1550 6600 1550 6900
Wire Wire Line
	1550 6900 2750 6900
Wire Wire Line
	2750 6900 2750 6700
Connection ~ 2750 6700
Entry Wire Line
	5600 6300 5700 6200
Entry Wire Line
	5600 6400 5700 6300
Entry Wire Line
	5600 6500 5700 6400
Entry Wire Line
	5600 6600 5700 6500
Entry Wire Line
	5600 6700 5700 6600
Entry Wire Line
	5600 6800 5700 6700
Entry Wire Line
	5600 6900 5700 6800
Entry Wire Line
	5600 7000 5700 6900
Wire Wire Line
	4350 6800 4250 6800
Wire Wire Line
	4250 6800 4250 7100
Wire Bus Line
	5700 6200 6550 6200
Entry Wire Line
	6650 6000 6550 6100
Entry Wire Line
	6650 5900 6550 6000
Entry Wire Line
	6650 5800 6550 5900
Entry Wire Line
	6650 5700 6550 5800
Entry Wire Line
	6650 5600 6550 5700
Entry Wire Line
	6650 5500 6550 5600
Entry Wire Line
	6650 5400 6550 5500
Entry Wire Line
	6650 5300 6550 5400
Wire Wire Line
	5600 6300 5350 6300
Wire Wire Line
	5600 6400 5350 6400
Wire Wire Line
	5600 6500 5350 6500
Wire Wire Line
	5600 6600 5350 6600
Wire Wire Line
	5600 6700 5350 6700
Wire Wire Line
	5600 7000 5350 7000
Wire Wire Line
	4250 7100 5350 7100
Wire Wire Line
	5350 7100 5350 7000
Wire Wire Line
	6650 6000 6900 6000
Wire Wire Line
	6650 5900 6900 5900
Wire Wire Line
	6650 5800 6900 5800
Wire Wire Line
	6650 5700 6900 5700
Wire Wire Line
	6650 5600 6900 5600
Wire Wire Line
	6900 5500 6650 5500
Wire Wire Line
	6900 5400 6650 5400
Wire Wire Line
	6650 5300 6900 5300
Text Label 5400 6300 0    50   ~ 0
M0
Text Label 5400 6400 0    50   ~ 0
M1
Text Label 5400 6500 0    50   ~ 0
M2
Text Label 5400 6600 0    50   ~ 0
M3
Text Label 5400 6700 0    50   ~ 0
M4
Text Label 5400 6800 0    50   ~ 0
M5
Text Label 5400 6900 0    50   ~ 0
M6
Text Label 5400 7000 0    50   ~ 0
M7
Text Label 6700 5300 0    50   ~ 0
M0
Text Label 6700 5400 0    50   ~ 0
M1
Text Label 6700 5500 0    50   ~ 0
M2
Text Label 6700 5600 0    50   ~ 0
M3
Text Label 6700 5700 0    50   ~ 0
M4
Text Label 6700 5800 0    50   ~ 0
M5
Text Label 6700 5900 0    50   ~ 0
M6
Text Label 6700 6000 0    50   ~ 0
M7
Wire Wire Line
	4350 6900 4150 6900
Wire Wire Line
	4150 6900 4150 6600
Wire Wire Line
	4150 6600 4350 6600
Wire Wire Line
	550  3300 950  3300
Text Label 5750 3100 0    50   ~ 0
5V
Text Label 5750 3200 0    50   ~ 0
3.3V
Text Label 5750 3300 0    50   ~ 0
GND
NoConn ~ 5500 4300
Wire Wire Line
	5500 3900 5600 3900
Wire Wire Line
	5600 3900 5600 4100
Wire Wire Line
	5600 4100 5500 4100
Wire Wire Line
	4400 4300 3800 4300
Wire Wire Line
	3800 4300 3800 3500
Connection ~ 3800 3300
Wire Wire Line
	3800 3300 6300 3300
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_6
U 1 1 62368A89
P 4550 3500
F 0 "C1.0nF_6" H 4600 3350 50  0000 L CNN
F 1 "885012207033" H 4800 3674 50  0001 C CNN
F 2 "CAPC2012X90N" H 4900 3550 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 4900 3450 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 4900 3350 50  0001 L CNN "Description"
F 5 "0.9" H 4900 3250 50  0001 L CNN "Height"
F 6 "710-885012207033" H 4900 3150 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 4900 3050 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 4900 2950 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 4900 2850 50  0001 L CNN "Manufacturer_Part_Number"
	1    4550 3500
	-1   0    0    1   
$EndComp
Wire Wire Line
	5600 3900 5600 3800
Connection ~ 5600 3900
Connection ~ 5600 3200
Wire Wire Line
	5600 3200 5700 3200
Wire Wire Line
	5500 3800 5600 3800
Connection ~ 5600 3800
Wire Wire Line
	5600 3800 5600 3200
Wire Wire Line
	5600 4100 5600 4200
Wire Wire Line
	5600 4200 5500 4200
Connection ~ 5600 4100
Wire Wire Line
	5500 3700 5500 3500
Connection ~ 5500 3100
Wire Wire Line
	5500 3100 11150 3100
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_4
U 1 1 62402C8A
P 2200 4200
F 0 "C1.0nF_4" H 2250 4050 50  0000 L CNN
F 1 "885012207033" H 2450 4374 50  0001 C CNN
F 2 "CAPC2012X90N" H 2550 4250 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 2550 4150 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 2550 4050 50  0001 L CNN "Description"
F 5 "0.9" H 2550 3950 50  0001 L CNN "Height"
F 6 "710-885012207033" H 2550 3850 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 2550 3750 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 2550 3650 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 2550 3550 50  0001 L CNN "Manufacturer_Part_Number"
	1    2200 4200
	-1   0    0    1   
$EndComp
NoConn ~ 1450 4700
Wire Wire Line
	1450 4600 1450 4500
Wire Wire Line
	1450 3200 1450 4500
Connection ~ 1450 4500
Connection ~ 1450 3200
Wire Wire Line
	1450 3200 2550 3200
Wire Wire Line
	2450 4500 2550 4500
Wire Wire Line
	2550 4500 2550 4200
Connection ~ 2550 3200
Wire Wire Line
	2550 3200 2650 3200
Wire Wire Line
	1450 4800 1350 4800
Wire Wire Line
	1350 4800 1350 4200
Connection ~ 1350 3300
Wire Wire Line
	1350 3300 3100 3300
$Comp
L Wurth_Elektronik:885012207027 C0.1nF_2
U 1 1 624564D3
P 4900 1300
F 0 "C0.1nF_2" V 5150 1050 50  0000 L CNN
F 1 "885012207027" V 5250 700 50  0001 L CNN
F 2 "CAPC2012X90N" H 5250 1350 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207027.pdf" H 5250 1250 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 100pF X7R0805101K016DFCT10000" H 5250 1150 50  0001 L CNN "Description"
F 5 "0.9" H 5250 1050 50  0001 L CNN "Height"
F 6 "710-885012207027" H 5250 950 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207027?qs=0KOYDY2FL28IWYVDh0DkrQ%3D%3D" H 5250 850 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 5250 750 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207027" H 5250 650 50  0001 L CNN "Manufacturer_Part_Number"
	1    4900 1300
	0    1    1    0   
$EndComp
Connection ~ 4900 1300
Wire Wire Line
	4900 1300 5150 1300
Connection ~ 7200 2900
Wire Wire Line
	7200 2900 11100 2900
Wire Wire Line
	7450 1400 8650 1400
Wire Wire Line
	8550 1200 11150 1200
Wire Wire Line
	9400 1300 10900 1300
Wire Wire Line
	3050 2750 7450 2750
Wire Wire Line
	4250 1300 4900 1300
Connection ~ 4250 1300
Connection ~ 2950 1400
Wire Wire Line
	4900 1800 4900 2450
Wire Wire Line
	4900 2450 5150 2450
Connection ~ 5150 2450
Connection ~ 2650 3200
Wire Wire Line
	2650 3200 2950 3200
Wire Wire Line
	1650 6200 1050 6200
Wire Wire Line
	1050 6200 1050 3200
Connection ~ 1650 6200
Connection ~ 1050 3200
Wire Wire Line
	1650 6700 950  6700
Wire Wire Line
	950  6700 950  3300
Connection ~ 950  3300
Wire Wire Line
	950  3300 1350 3300
Wire Wire Line
	800  6700 950  6700
Connection ~ 950  6700
NoConn ~ 2650 6100
Wire Wire Line
	5400 4850 5500 4850
Wire Wire Line
	5700 4850 5700 3200
Connection ~ 5700 3200
Wire Wire Line
	5700 3200 5800 3200
Wire Wire Line
	5400 4950 5500 4950
Wire Wire Line
	5500 4950 5500 4850
Connection ~ 5500 4850
Wire Wire Line
	5500 4850 5700 4850
Wire Wire Line
	5500 5350 5400 5350
Wire Wire Line
	5500 4950 5500 5150
Connection ~ 5500 4950
Wire Wire Line
	5400 5150 5500 5150
Connection ~ 5500 5150
Wire Wire Line
	5500 5150 5500 5350
$Comp
L Nexperia:74LVC04AD,118 LVC04
U 1 1 61C688F8
P 4400 4850
F 0 "LVC04" H 4900 4550 50  0000 C CNN
F 1 "74LVC04AD,118" H 4900 4100 50  0000 C CNN
F 2 "SOIC127P600X175-14N" H 5250 4950 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC04A.pdf" H 5250 4850 50  0001 L CNN
F 4 "74LVC04A - Hex inverter@en-us" H 5250 4750 50  0001 L CNN "Description"
F 5 "1.75" H 5250 4650 50  0001 L CNN "Height"
F 6 "771-74LVC04AD-T" H 5250 4550 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC04AD118?qs=me8TqzrmIYWth3Q0Nudt8A%3D%3D" H 5250 4450 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 5250 4350 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC04AD,118" H 5250 4250 50  0001 L CNN "Manufacturer_Part_Number"
	1    4400 4850
	1    0    0    -1  
$EndComp
NoConn ~ 5400 5450
NoConn ~ 5400 5250
NoConn ~ 5400 5050
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_5
U 1 1 626A6A20
P 3250 3200
F 0 "C1.0nF_5" V 3500 3100 50  0000 L CNN
F 1 "885012207033" H 3500 3374 50  0001 C CNN
F 2 "CAPC2012X90N" H 3600 3250 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 3600 3150 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 3600 3050 50  0001 L CNN "Description"
F 5 "0.9" H 3600 2950 50  0001 L CNN "Height"
F 6 "710-885012207033" H 3600 2850 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 3600 2750 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 3600 2650 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 3600 2550 50  0001 L CNN "Manufacturer_Part_Number"
	1    3250 3200
	0    1    1    0   
$EndComp
Connection ~ 3250 3200
Wire Wire Line
	3250 3200 5600 3200
Wire Wire Line
	4400 5450 3450 5450
Wire Wire Line
	3450 5450 3450 3300
Connection ~ 3450 3300
Wire Wire Line
	3450 3300 3800 3300
Wire Wire Line
	3250 3700 3250 5450
Wire Wire Line
	3250 5450 3450 5450
Connection ~ 3450 5450
NoConn ~ 5500 4000
NoConn ~ 4400 3900
Connection ~ 6300 3300
Wire Wire Line
	6300 3300 8700 3300
Wire Wire Line
	6300 6100 6300 4650
Wire Wire Line
	6900 5200 6900 4800
Wire Wire Line
	6900 4800 7900 4800
Wire Wire Line
	7900 4800 7900 5200
Wire Wire Line
	7900 4800 7900 4650
Connection ~ 7900 4800
Connection ~ 7900 3200
Wire Wire Line
	9800 5200 9800 4800
Connection ~ 9800 3200
Wire Wire Line
	9800 3200 11150 3200
Wire Wire Line
	8800 5200 8800 4800
Wire Wire Line
	8800 4800 9800 4800
Connection ~ 9800 4800
Wire Wire Line
	9800 4800 9800 4650
Wire Wire Line
	6000 5650 6000 6300
Wire Wire Line
	6000 6300 7900 6300
Wire Wire Line
	7900 6300 7900 6100
Connection ~ 6000 5650
Wire Wire Line
	6450 6400 9800 6400
Wire Wire Line
	9800 6400 9800 6100
Wire Wire Line
	7900 6000 8800 6000
Wire Wire Line
	8800 5900 7900 5900
Wire Wire Line
	7900 5800 8800 5800
Wire Wire Line
	7900 5700 8400 5700
Wire Wire Line
	8800 5500 8200 5500
Wire Wire Line
	7900 5400 8100 5400
Wire Wire Line
	8800 5300 8000 5300
Wire Wire Line
	8000 5300 8000 3500
Wire Wire Line
	8000 3500 10550 3500
Connection ~ 8000 5300
Wire Wire Line
	8000 5300 7900 5300
Wire Wire Line
	9800 5300 9900 5300
Wire Wire Line
	9900 5300 9900 3600
Wire Wire Line
	9900 3600 10550 3600
Wire Wire Line
	8800 6100 8700 6100
Wire Wire Line
	8700 6100 8700 4650
Connection ~ 8700 3300
Wire Wire Line
	8700 3300 11150 3300
NoConn ~ 9800 6000
NoConn ~ 9800 5900
NoConn ~ 9800 5800
Wire Wire Line
	9800 5400 10000 5400
Wire Wire Line
	10000 5400 10000 3800
Wire Wire Line
	9800 5500 10100 5500
Wire Wire Line
	10100 4000 10550 4000
Wire Wire Line
	10000 3800 10550 3800
Connection ~ 8100 5400
Wire Wire Line
	8100 5400 8800 5400
Wire Wire Line
	8100 3700 8100 5400
Wire Wire Line
	7900 5600 8300 5600
Wire Wire Line
	8200 5500 8200 3900
Connection ~ 8200 5500
Wire Wire Line
	8200 5500 7900 5500
Wire Wire Line
	8300 5600 8300 4100
Wire Wire Line
	8300 4100 10550 4100
Connection ~ 8300 5600
Wire Wire Line
	8300 5600 8800 5600
Wire Wire Line
	8400 5700 8400 4300
Connection ~ 8400 5700
Wire Wire Line
	8400 5700 8800 5700
Wire Wire Line
	9800 5600 10200 5600
Wire Wire Line
	10200 4200 10550 4200
Wire Wire Line
	9800 5700 10300 5700
Wire Wire Line
	10300 5700 10300 4400
Wire Wire Line
	10300 4400 10550 4400
Wire Wire Line
	6450 6400 6450 3400
Wire Wire Line
	6450 3400 7350 3400
Wire Wire Line
	7350 3400 7350 2450
Wire Wire Line
	4150 6900 3100 6900
Wire Wire Line
	3100 6900 3100 3300
Connection ~ 4150 6900
Connection ~ 3100 3300
Wire Wire Line
	3100 3300 3450 3300
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_10
U 1 1 62FA640D
P 2950 3200
F 0 "C1.0nF_10" V 3200 3100 50  0000 L CNN
F 1 "885012207033" H 3200 3374 50  0001 C CNN
F 2 "CAPC2012X90N" H 3300 3250 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 3300 3150 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 3300 3050 50  0001 L CNN "Description"
F 5 "0.9" H 3300 2950 50  0001 L CNN "Height"
F 6 "710-885012207033" H 3300 2850 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 3300 2750 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 3300 2650 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 3300 2550 50  0001 L CNN "Manufacturer_Part_Number"
	1    2950 3200
	0    1    1    0   
$EndComp
Connection ~ 2950 3200
Wire Wire Line
	2950 3200 3250 3200
Wire Wire Line
	2950 3700 2950 6900
Wire Wire Line
	2950 6900 3100 6900
Connection ~ 3100 6900
$Comp
L Oscillator:XUX51 CLOCK1
U 1 1 62FCA20A
P 1900 2400
F 0 "CLOCK1" H 2050 2650 50  0000 C CNN
F 1 "XUX51" H 2050 2150 50  0000 C CNN
F 2 "Oscillator:Oscillator_SMD_IDT_JS6-6_5.0x3.2mm_P1.27mm" H 1950 2050 50  0001 C CNN
F 3 "https://www.idt.com/document/dst/xu-family-datasheet" H 1500 3100 50  0001 C CNN
	1    1900 2400
	1    0    0    -1  
$EndComp
NoConn ~ 2300 2500
NoConn ~ -850 2000
$Comp
L Connector:Conn_01x02_Male J4
U 1 1 6303295C
P 8350 2350
F 0 "J4" H 8250 2300 50  0000 C CNN
F 1 "Conn_01x02_Male" H 8458 2440 50  0000 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x02_P2.54mm_Vertical" H 8350 2350 50  0001 C CNN
F 3 "~" H 8350 2350 50  0001 C CNN
	1    8350 2350
	1    0    0    -1  
$EndComp
Wire Wire Line
	8650 1400 8650 2350
Wire Wire Line
	8650 2350 8550 2350
Connection ~ 8650 1400
Wire Wire Line
	8650 1400 9250 1400
Wire Wire Line
	8750 2000 8750 2450
Wire Wire Line
	8750 2450 8550 2450
Connection ~ 8750 2000
Wire Wire Line
	8750 2000 9500 2000
Wire Wire Line
	5350 6800 5600 6800
Wire Wire Line
	5350 6900 5600 6900
Wire Wire Line
	5350 6200 5600 6200
Wire Wire Line
	5600 6200 5600 6100
Wire Wire Line
	5600 6100 5800 6100
Wire Wire Line
	5800 6100 5800 3200
Connection ~ 5800 3200
$Comp
L Connector:Conn_01x10_Male J6
U 1 1 6344B440
P 10750 4000
F 0 "J6" H 10850 3350 50  0000 C CNN
F 1 "Conn_01x10_Male" H 10800 4500 50  0000 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x10_P2.54mm_Vertical" H 10750 4000 50  0001 C CNN
F 3 "~" H 10750 4000 50  0001 C CNN
	1    10750 4000
	-1   0    0    1   
$EndComp
Wire Wire Line
	10100 4000 10100 5500
Wire Wire Line
	10200 4200 10200 5600
Wire Wire Line
	8850 4650 8700 4650
Connection ~ 8700 4650
Wire Wire Line
	8700 3300 8700 4650
Wire Wire Line
	8200 3900 10550 3900
Wire Wire Line
	8400 4300 10550 4300
$Comp
L Connector:Conn_01x02_Male J5
U 1 1 6377AE00
P 8350 2700
F 0 "J5" H 8250 2650 50  0000 C CNN
F 1 "Conn_01x02_Male" H 8458 2790 50  0000 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x02_P2.54mm_Vertical" H 8350 2700 50  0001 C CNN
F 3 "~" H 8350 2700 50  0001 C CNN
	1    8350 2700
	1    0    0    -1  
$EndComp
Wire Wire Line
	8550 2800 8650 2800
Wire Wire Line
	8650 2800 8650 3000
Connection ~ 8650 3000
Wire Wire Line
	8650 3000 2750 3000
Wire Wire Line
	8550 2700 9400 2700
Connection ~ 9400 2700
Wire Wire Line
	9400 2700 9400 2800
Wire Wire Line
	800  3200 1050 3200
Connection ~ 800  3200
Wire Wire Line
	550  3200 800  3200
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_3
U 1 1 6254DD77
P 800 6200
F 0 "C1.0nF_3" V 1050 6050 50  0000 L CNN
F 1 "885012207033" H 1050 6374 50  0001 C CNN
F 2 "CAPC2012X90N" H 1150 6250 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 1150 6150 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 1150 6050 50  0001 L CNN "Description"
F 5 "0.9" H 1150 5950 50  0001 L CNN "Height"
F 6 "710-885012207033" H 1150 5850 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 1150 5750 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 1150 5650 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 1150 5550 50  0001 L CNN "Manufacturer_Part_Number"
	1    800  6200
	0    1    1    0   
$EndComp
Wire Wire Line
	800  6200 800  3200
Wire Wire Line
	8100 3700 10550 3700
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_8
U 1 1 62DF072A
P 9350 4650
F 0 "C1.0nF_8" H 9450 4500 50  0000 L CNN
F 1 "885012207033" H 9600 4824 50  0001 C CNN
F 2 "CAPC2012X90N" H 9700 4700 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 9700 4600 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 9700 4500 50  0001 L CNN "Description"
F 5 "0.9" H 9700 4400 50  0001 L CNN "Height"
F 6 "710-885012207033" H 9700 4300 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 9700 4200 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 9700 4100 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 9700 4000 50  0001 L CNN "Manufacturer_Part_Number"
	1    9350 4650
	-1   0    0    1   
$EndComp
Wire Wire Line
	9350 4650 9800 4650
Connection ~ 9800 4650
Wire Wire Line
	9800 4650 9800 3200
Wire Wire Line
	7900 3200 9800 3200
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_7
U 1 1 6271AAC0
P 7400 4650
F 0 "C1.0nF_7" H 7500 4500 50  0000 L CNN
F 1 "885012207033" H 7650 4824 50  0001 C CNN
F 2 "CAPC2012X90N" H 7750 4700 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 7750 4600 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 7750 4500 50  0001 L CNN "Description"
F 5 "0.9" H 7750 4400 50  0001 L CNN "Height"
F 6 "710-885012207033" H 7750 4300 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 7750 4200 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 7750 4100 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 7750 4000 50  0001 L CNN "Manufacturer_Part_Number"
	1    7400 4650
	-1   0    0    1   
$EndComp
Wire Wire Line
	5800 3200 7900 3200
Wire Wire Line
	6300 4650 6900 4650
Connection ~ 6300 4650
Wire Wire Line
	6300 4650 6300 3300
Wire Wire Line
	7400 4650 7900 4650
Connection ~ 7900 4650
Wire Wire Line
	7900 4650 7900 3200
Wire Wire Line
	1250 750  1500 750 
Wire Wire Line
	1050 950  1500 950 
Wire Wire Line
	1050 850  1500 850 
Wire Wire Line
	1500 950  2000 950 
Connection ~ 1500 950 
Connection ~ 2000 950 
Wire Wire Line
	2000 850  1500 850 
Connection ~ 2000 850 
Connection ~ 1500 850 
Wire Wire Line
	1500 750  2000 750 
Connection ~ 1500 750 
Connection ~ 2000 750 
Wire Wire Line
	550  1200 3150 1200
Wire Wire Line
	2950 1400 5400 1400
Wire Wire Line
	2900 550  3150 550 
Wire Wire Line
	3150 550  3150 1200
Connection ~ 2900 550 
Connection ~ 3150 1200
Wire Wire Line
	3150 1200 8550 1200
Text Label 10350 3500 0    50   ~ 0
SEL0
Text Label 10350 3600 0    50   ~ 0
SEL1
Text Label 10350 3700 0    50   ~ 0
SEL2
Text Label 10350 3800 0    50   ~ 0
SEL3
Text Label 10350 3900 0    50   ~ 0
SEL4
Text Label 10350 4000 0    50   ~ 0
SEL5
Text Label 10350 4100 0    50   ~ 0
SEL6
Text Label 10350 4200 0    50   ~ 0
SEL7
Text Label 10350 4300 0    50   ~ 0
SEL8
Text Label 10350 4400 0    50   ~ 0
SEL9
Text Label 8450 5800 0    50   ~ 0
D5
Text Label 8450 5900 0    50   ~ 0
D6
Text Label 8450 6000 0    50   ~ 0
D7
Text Label 3250 6200 0    50   ~ 0
CO0
Text Label 3250 6300 0    50   ~ 0
CO1
Text Label 3250 6400 0    50   ~ 0
CO2
Text Label 3250 6500 0    50   ~ 0
CO3
Text Label 2450 4600 0    50   ~ 0
OR_Out
Text Label 4400 4200 2    50   ~ 0
AND_Out
Wire Wire Line
	4050 3500 3800 3500
Connection ~ 3800 3500
Wire Wire Line
	3800 3500 3800 3300
Wire Wire Line
	550  3100 5500 3100
Wire Wire Line
	5500 3500 4550 3500
Connection ~ 5500 3500
Wire Wire Line
	5500 3500 5500 3100
Wire Wire Line
	2650 3200 2650 6000
Wire Wire Line
	1700 4200 1350 4200
Connection ~ 1350 4200
Wire Wire Line
	1350 4200 1350 3300
Wire Wire Line
	1050 3200 1450 3200
Wire Wire Line
	2200 4200 2550 4200
Connection ~ 2550 4200
Wire Wire Line
	2550 4200 2550 3200
Wire Wire Line
	4400 4200 3650 4200
Wire Wire Line
	3650 4200 3650 4800
Wire Wire Line
	3650 4800 2450 4800
Wire Wire Line
	2550 4600 2550 5550
Wire Wire Line
	9900 1650 9250 1650
Wire Wire Line
	5300 1300 9400 1300
Connection ~ 9250 1650
Wire Wire Line
	9250 1650 9250 1400
Wire Wire Line
	10400 1650 10900 1650
Connection ~ 10900 1650
Wire Wire Line
	10900 1650 10900 1300
Wire Wire Line
	550  1300 1150 1300
Wire Wire Line
	1150 1800 1150 1300
Connection ~ 1150 1300
Wire Wire Line
	1150 1300 1400 1300
Wire Wire Line
	3200 1900 3150 1900
Wire Wire Line
	2400 1900 2400 2400
Wire Wire Line
	2400 2400 2300 2400
Wire Wire Line
	6300 6100 6900 6100
Wire Bus Line
	5700 6200 5700 6900
Wire Bus Line
	6550 5400 6550 6200
Connection ~ 3150 1900
Wire Wire Line
	3150 1900 2400 1900
$Comp
L Nexperia:74LVC2G32DC-Q100H LVC32
U 1 1 61CAC7F2
P 1450 4500
F 0 "LVC32" H 1950 4350 50  0000 C CNN
F 1 "74LVC2G32DC-Q100H" H 1950 4050 50  0000 C CNN
F 2 "SOP50P310X100-8N" H 2300 4600 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC2G32_Q100.pdf" H 2300 4500 50  0001 L CNN
F 4 "74LVC2G32-Q100 - Dual 2-input OR gate@en-us" H 2300 4400 50  0001 L CNN "Description"
F 5 "1" H 2300 4300 50  0001 L CNN "Height"
F 6 "771-74LVC2G32DCQ100H" H 2300 4200 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC2G32DC-Q100H?qs=fi7yB2oewZkpBkB%252Bo1Xtow%3D%3D" H 2300 4100 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 2300 4000 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC2G32DC-Q100H" H 2300 3900 50  0001 L CNN "Manufacturer_Part_Number"
	1    1450 4500
	1    0    0    -1  
$EndComp
$EndSCHEMATC
