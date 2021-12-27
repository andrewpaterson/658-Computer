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
Wire Wire Line
	4450 1900 4350 1900
Wire Wire Line
	4450 1900 4450 2000
Wire Wire Line
	4450 2000 4350 2000
Wire Wire Line
	4350 2100 4450 2100
Wire Wire Line
	4450 2100 4450 2200
Wire Wire Line
	4450 2200 4350 2200
Wire Wire Line
	4350 2300 4450 2300
Wire Wire Line
	4450 2300 4450 2400
Wire Wire Line
	4450 2400 4350 2400
Wire Wire Line
	3350 2000 3250 2000
Wire Wire Line
	3250 2000 3250 2100
Wire Wire Line
	3250 2100 3350 2100
Wire Wire Line
	3350 2200 3250 2200
Wire Wire Line
	3250 2200 3250 2300
Wire Wire Line
	3250 2300 3350 2300
Wire Wire Line
	6800 1850 6900 1850
Connection ~ 6900 1850
Wire Wire Line
	6900 2250 6800 2250
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
L Connector:Conn_01x02_Male J_HClock1
U 1 1 61C9B9E9
P 4900 2250
F 0 "J_HClock1" H 5000 2400 50  0000 C CNN
F 1 "Conn_01x02_Male" H 4950 2100 50  0001 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x02_P2.54mm_Vertical" H 4900 2250 50  0001 C CNN
F 3 "~" H 4900 2250 50  0001 C CNN
	1    4900 2250
	1    0    0    -1  
$EndComp
Wire Wire Line
	5100 2250 5700 2250
Wire Wire Line
	5500 2350 5100 2350
Connection ~ 5500 2350
Wire Wire Line
	1000 2250 900  2250
$Comp
L Connector_Generic:Conn_02x03_Odd_Even J_Power1
U 1 1 61C5DE69
P 1800 850
F 0 "J_Power1" H 1850 650 50  0000 C CNN
F 1 "Conn_02x03" H 1850 600 50  0001 C CNN
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
	2150 550  2900 550 
Wire Wire Line
	2400 950  2900 950 
Connection ~ 2400 950 
NoConn ~ 4350 2500
Wire Wire Line
	3150 2500 3150 1650
Wire Wire Line
	3150 2500 3350 2500
Wire Wire Line
	4450 1900 4450 1650
Connection ~ 4450 1900
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_1
U 1 1 61C6AFA3
P 4100 1350
F 0 "C1.0nF_1" H 4200 1200 50  0000 L CNN
F 1 "885012207033" H 4350 1524 50  0001 C CNN
F 2 "CAPC2012X90N" H 4450 1400 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 4450 1300 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 4450 1200 50  0001 L CNN "Description"
F 5 "0.9" H 4450 1100 50  0001 L CNN "Height"
F 6 "710-885012207033" H 4450 1000 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 4450 900 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 4450 800 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 4450 700 50  0001 L CNN "Manufacturer_Part_Number"
	1    4100 1350
	-1   0    0    1   
$EndComp
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_2
U 1 1 61C74217
P 6500 1100
F 0 "C1.0nF_2" H 6600 950 50  0000 L CNN
F 1 "885012207033" H 6750 1274 50  0001 C CNN
F 2 "CAPC2012X90N" H 6850 1150 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 6850 1050 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 6850 950 50  0001 L CNN "Description"
F 5 "0.9" H 6850 850 50  0001 L CNN "Height"
F 6 "710-885012207033" H 6850 750 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 6850 650 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 6850 550 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 6850 450 50  0001 L CNN "Manufacturer_Part_Number"
	1    6500 1100
	-1   0    0    1   
$EndComp
Wire Wire Line
	3350 2400 3250 2400
Wire Wire Line
	3250 2400 3250 2750
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
L Nexperia:74LVCU04APW,118 LVCU4
U 1 1 61C7F1FB
P 3350 1900
F 0 "LVCU4" H 3850 1600 50  0000 C CNN
F 1 "74LVCU04APW,118" H 3850 1150 50  0000 C CNN
F 2 "SOP65P640X110-14N" H 4200 2000 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVCU04A.pdf" H 4200 1900 50  0001 L CNN
F 4 "74LVCU04A - Hex unbuffered inverter@en-us" H 4200 1800 50  0001 L CNN "Description"
F 5 "1.1" H 4200 1700 50  0001 L CNN "Height"
F 6 "771-LVCU04APW118" H 4200 1600 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVCU04APW118?qs=me8TqzrmIYXibrDyFt%252BDyw%3D%3D" H 4200 1500 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 4200 1400 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVCU04APW,118" H 4200 1300 50  0001 L CNN "Manufacturer_Part_Number"
	1    3350 1900
	1    0    0    -1  
$EndComp
Wire Wire Line
	4400 5050 3750 5050
Wire Wire Line
	3750 5050 3750 6800
Connection ~ 3750 6800
Wire Wire Line
	3750 6800 4350 6800
Wire Wire Line
	4400 5250 3850 5250
Wire Wire Line
	3850 5250 3850 6900
Connection ~ 3850 6900
Wire Wire Line
	3850 6900 4350 6900
Wire Wire Line
	4400 5450 4150 5450
Wire Wire Line
	4150 5450 4150 7100
Connection ~ 4150 7100
Wire Wire Line
	4150 7100 4350 7100
Wire Wire Line
	3950 7000 3950 4000
Wire Wire Line
	3950 4000 4400 4000
Connection ~ 3950 7000
Wire Wire Line
	3950 7000 4350 7000
Wire Wire Line
	4400 5550 4050 5550
Wire Wire Line
	4050 5550 4050 4100
Wire Wire Line
	4050 4100 4400 4100
Wire Wire Line
	4400 5350 4150 5350
Wire Wire Line
	4150 5350 4150 3800
Wire Wire Line
	4150 3800 4400 3800
Wire Wire Line
	4400 3700 4250 3700
Wire Wire Line
	4250 3700 4250 5150
Wire Wire Line
	4250 5150 4400 5150
$Comp
L Maxim:DS1813R-10+T&R DS1813
U 1 1 61CEDE4E
P 8800 1750
F 0 "DS1813" H 9400 1650 50  0000 C CNN
F 1 "DS1813R-10+T&R" H 9350 1900 50  0000 C CNN
F 2 "SOT95P237X112-3N" H 9750 1850 50  0001 L CNN
F 3 "https://componentsearchengine.com/Datasheets/2/DS1813R-10+T&R.pdf" H 9750 1750 50  0001 L CNN
F 4 "Maxim DS1813R-10+T&R, Voltage Supervisor 4.25  4.49 V, Reset Input, 3-Pin SOT-23" H 9750 1650 50  0001 L CNN "Description"
F 5 "1.12" H 9750 1550 50  0001 L CNN "Height"
F 6 "700-DS1813R-10T&R" H 9750 1450 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Maxim-Integrated/DS1813R-10%2bTR?qs=0Y9aZN%252BMVCUEX%2FgvPX%252B6Nw%3D%3D" H 9750 1350 50  0001 L CNN "Mouser Price/Stock"
F 8 "Maxim Integrated" H 9750 1250 50  0001 L CNN "Manufacturer_Name"
F 9 "DS1813R-10+T&R" H 9750 1150 50  0001 L CNN "Manufacturer_Part_Number"
	1    8800 1750
	-1   0    0    1   
$EndComp
$Comp
L Nexperia:74LVC74APW,118 LVC74_1
U 1 1 61CF938B
P 9600 1950
F 0 "LVC74_1" H 10150 1650 50  0000 C CNN
F 1 "74LVC74APW,118" H 10150 1200 50  0000 C CNN
F 2 "SOP65P640X110-14N" H 10550 2050 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC74A.pdf" H 10550 1950 50  0001 L CNN
F 4 "74LVC74A - Dual D-type flip-flop with set and reset; positive-edge trigger@en-us" H 10550 1850 50  0001 L CNN "Description"
F 5 "1.1" H 10550 1750 50  0001 L CNN "Height"
F 6 "771-74LVC74APW-T" H 10550 1650 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC74APW118?qs=me8TqzrmIYVtXwVfet0lzw%3D%3D" H 10550 1550 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 10550 1450 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC74APW,118" H 10550 1350 50  0001 L CNN "Manufacturer_Part_Number"
	1    9600 1950
	1    0    0    -1  
$EndComp
Wire Wire Line
	9600 2350 9400 2350
Wire Wire Line
	9400 2750 11100 2750
Wire Wire Line
	11100 2750 11100 2150
Wire Wire Line
	11100 2150 10700 2150
Connection ~ 7200 2350
Wire Wire Line
	7200 2350 7200 2650
$Comp
L Connector:Conn_01x02_Male J_Clock1
U 1 1 61CA7E7B
P 7750 2450
F 0 "J_Clock1" H 7750 2500 50  0000 C CNN
F 1 "Conn_01x02_Male" H 7650 2500 50  0001 C CNN
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
	7300 2450 7550 2450
Wire Wire Line
	6800 2450 7300 2450
Connection ~ 7300 2450
Wire Wire Line
	6800 2050 7300 2050
NoConn ~ 9600 2450
NoConn ~ 10700 2550
Wire Wire Line
	7700 1750 7700 1050
Wire Wire Line
	7200 2350 7550 2350
Wire Wire Line
	11000 2900 11000 2250
Wire Wire Line
	10700 2250 11000 2250
Wire Wire Line
	9600 1950 9500 1950
Wire Wire Line
	9600 2050 9500 2050
Wire Wire Line
	9500 2050 9500 1950
Connection ~ 9500 1950
Wire Wire Line
	10700 1950 10900 1950
Wire Wire Line
	10900 1950 10900 1650
Wire Wire Line
	9500 1950 9500 1750
Wire Wire Line
	9500 1750 10800 1750
Wire Wire Line
	10800 1750 10800 2050
Wire Wire Line
	10800 2050 10700 2050
Wire Wire Line
	10700 2350 10900 2350
Wire Wire Line
	10900 2350 10900 1950
Connection ~ 10900 1950
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_9
U 1 1 61E7D541
P 10400 1500
F 0 "C1.0nF_9" H 10500 1350 50  0000 L CNN
F 1 "885012207033" H 10650 1674 50  0001 C CNN
F 2 "CAPC2012X90N" H 10750 1550 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 10750 1450 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 10750 1350 50  0001 L CNN "Description"
F 5 "0.9" H 10750 1250 50  0001 L CNN "Height"
F 6 "710-885012207033" H 10750 1150 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 10750 1050 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 10750 950 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 10750 850 50  0001 L CNN "Manufacturer_Part_Number"
	1    10400 1500
	-1   0    0    1   
$EndComp
Connection ~ 7200 2650
Wire Wire Line
	7200 2650 7200 2900
Wire Wire Line
	9600 2250 9400 2250
Wire Wire Line
	9600 2550 9300 2550
Wire Wire Line
	9300 2550 9300 1500
Text Label 1100 750  0    50   ~ 0
5V
Text Label 1100 850  0    50   ~ 0
3.3V
Text Label 1100 950  0    50   ~ 0
GND
Text Label 6850 2350 0    50   ~ 0
Clock
Text Label 6850 2450 0    50   ~ 0
Clock_B
Wire Wire Line
	10700 2450 10800 2450
Text Label 10700 2450 0    50   ~ 0
Reset_1
Text Label 9600 2350 2    50   ~ 0
Reset_0
Wire Wire Line
	10800 3000 9150 3000
Wire Wire Line
	10800 2450 10800 3000
Wire Wire Line
	2000 4700 2400 4700
Wire Wire Line
	2400 4700 2400 3000
Connection ~ 2400 4700
Wire Wire Line
	2400 4700 2400 7300
Wire Wire Line
	2000 4600 2100 4600
Wire Wire Line
	1100 6600 1200 6600
Wire Wire Line
	1000 6700 1200 6700
Wire Wire Line
	1200 7200 1100 7200
Wire Wire Line
	1100 7200 1100 7500
Wire Wire Line
	1100 7500 2400 7500
Wire Wire Line
	2400 7500 2400 7300
Connection ~ 2400 7300
Wire Wire Line
	4350 7400 4250 7400
Wire Wire Line
	4250 7400 4250 7700
Entry Wire Line
	5800 6000 5700 6100
Entry Wire Line
	5800 5900 5700 6000
Entry Wire Line
	5800 5800 5700 5900
Entry Wire Line
	5800 5700 5700 5800
Entry Wire Line
	5800 5600 5700 5700
Entry Wire Line
	5800 5500 5700 5600
Entry Wire Line
	5800 5400 5700 5500
Entry Wire Line
	5800 5300 5700 5400
Wire Wire Line
	4250 7700 5350 7700
Wire Wire Line
	5350 7700 5350 7600
Wire Wire Line
	5800 6000 6900 6000
Wire Wire Line
	5800 5900 6900 5900
Wire Wire Line
	5800 5800 6900 5800
Wire Wire Line
	5800 5700 6900 5700
Wire Wire Line
	5800 5600 6900 5600
Wire Wire Line
	6900 5500 5800 5500
Wire Wire Line
	6900 5400 5800 5400
Wire Wire Line
	5800 5300 6900 5300
Text Label 5400 6900 0    50   ~ 0
M0
Text Label 5400 7000 0    50   ~ 0
M1
Text Label 5400 7100 0    50   ~ 0
M2
Text Label 5400 7200 0    50   ~ 0
M3
Text Label 5400 7300 0    50   ~ 0
M4
Text Label 5400 7400 0    50   ~ 0
M5
Text Label 5400 7500 0    50   ~ 0
M6
Text Label 5400 7600 0    50   ~ 0
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
	4350 7500 4150 7500
Wire Wire Line
	4150 7500 4150 7200
Wire Wire Line
	4150 7200 4350 7200
Text Label 5500 3450 2    50   ~ 0
5V
NoConn ~ 5500 4300
Wire Wire Line
	5500 3900 5600 3900
Wire Wire Line
	5600 3900 5600 4100
Wire Wire Line
	5600 4100 5500 4100
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_6
U 1 1 62368A89
P 5250 3450
F 0 "C1.0nF_6" H 5300 3300 50  0000 L CNN
F 1 "885012207033" H 5500 3624 50  0001 C CNN
F 2 "CAPC2012X90N" H 5600 3500 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 5600 3400 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 5600 3300 50  0001 L CNN "Description"
F 5 "0.9" H 5600 3200 50  0001 L CNN "Height"
F 6 "710-885012207033" H 5600 3100 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 5600 3000 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 5600 2900 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 5600 2800 50  0001 L CNN "Manufacturer_Part_Number"
	1    5250 3450
	-1   0    0    1   
$EndComp
Wire Wire Line
	5600 3900 5600 3800
Connection ~ 5600 3900
Wire Wire Line
	5500 3800 5600 3800
Connection ~ 5600 3800
Wire Wire Line
	5600 4100 5600 4200
Wire Wire Line
	5600 4200 5500 4200
Connection ~ 5600 4100
Wire Wire Line
	5500 3700 5500 3450
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_4
U 1 1 62402C8A
P 1750 4150
F 0 "C1.0nF_4" H 1800 4000 50  0000 L CNN
F 1 "885012207033" H 2000 4324 50  0001 C CNN
F 2 "CAPC2012X90N" H 2100 4200 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 2100 4100 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 2100 4000 50  0001 L CNN "Description"
F 5 "0.9" H 2100 3900 50  0001 L CNN "Height"
F 6 "710-885012207033" H 2100 3800 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 2100 3700 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 2100 3600 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 2100 3500 50  0001 L CNN "Manufacturer_Part_Number"
	1    1750 4150
	-1   0    0    1   
$EndComp
NoConn ~ 1000 4700
$Comp
L Wurth_Elektronik:885012207027 C0.1nF_2
U 1 1 624564D3
P 6500 1400
F 0 "C0.1nF_2" H 6600 1250 50  0000 L CNN
F 1 "885012207027" V 6850 800 50  0001 L CNN
F 2 "CAPC2012X90N" H 6850 1450 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207027.pdf" H 6850 1350 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 100pF X7R0805101K016DFCT10000" H 6850 1250 50  0001 L CNN "Description"
F 5 "0.9" H 6850 1150 50  0001 L CNN "Height"
F 6 "710-885012207027" H 6850 1050 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207027?qs=0KOYDY2FL28IWYVDh0DkrQ%3D%3D" H 6850 950 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 6850 850 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207027" H 6850 750 50  0001 L CNN "Manufacturer_Part_Number"
	1    6500 1400
	-1   0    0    1   
$EndComp
Connection ~ 7200 2900
Wire Wire Line
	7200 2900 11000 2900
Wire Wire Line
	3250 2750 7450 2750
Wire Wire Line
	1200 7300 800  7300
Wire Wire Line
	800  7300 800  6250
NoConn ~ 2200 6700
Wire Wire Line
	5400 5050 5500 5050
Wire Wire Line
	5400 5150 5500 5150
Wire Wire Line
	5500 5150 5500 5050
Connection ~ 5500 5050
Wire Wire Line
	5500 5550 5400 5550
Wire Wire Line
	5500 5150 5500 5350
Connection ~ 5500 5150
Wire Wire Line
	5400 5350 5500 5350
Connection ~ 5500 5350
Wire Wire Line
	5500 5350 5500 5550
NoConn ~ 5400 5650
NoConn ~ 5400 5450
NoConn ~ 5400 5250
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_5
U 1 1 626A6A20
P 5150 4750
F 0 "C1.0nF_5" H 5250 4650 50  0000 L CNN
F 1 "885012207033" H 5400 4924 50  0001 C CNN
F 2 "CAPC2012X90N" H 5500 4800 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 5500 4700 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 5500 4600 50  0001 L CNN "Description"
F 5 "0.9" H 5500 4500 50  0001 L CNN "Height"
F 6 "710-885012207033" H 5500 4400 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 5500 4300 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 5500 4200 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 5500 4100 50  0001 L CNN "Manufacturer_Part_Number"
	1    5150 4750
	-1   0    0    1   
$EndComp
Wire Wire Line
	4400 5650 3650 5650
Wire Wire Line
	3650 5650 3650 4750
NoConn ~ 5500 4000
NoConn ~ 4400 3900
Wire Wire Line
	7900 6000 8800 6000
Wire Wire Line
	8800 5900 7900 5900
Wire Wire Line
	7900 5800 8800 5800
Wire Wire Line
	8700 3500 10550 3500
Wire Wire Line
	9800 5300 9900 5300
Wire Wire Line
	9900 5300 9900 3600
Wire Wire Line
	9900 3600 10550 3600
Wire Wire Line
	8800 6100 8700 6100
Wire Wire Line
	8700 6100 8700 5200
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
Wire Wire Line
	8700 4100 10550 4100
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
	6100 6400 6100 3400
Wire Wire Line
	6100 3400 7300 3400
Wire Wire Line
	7300 3400 7300 2450
Wire Wire Line
	4150 7500 3650 7500
Connection ~ 4150 7500
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_10
U 1 1 62FA640D
P 5100 6550
F 0 "C1.0nF_10" H 5150 6400 50  0000 L CNN
F 1 "885012207033" H 5350 6724 50  0001 C CNN
F 2 "CAPC2012X90N" H 5450 6600 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 5450 6500 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 5450 6400 50  0001 L CNN "Description"
F 5 "0.9" H 5450 6300 50  0001 L CNN "Height"
F 6 "710-885012207033" H 5450 6200 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 5450 6100 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 5450 6000 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 5450 5900 50  0001 L CNN "Manufacturer_Part_Number"
	1    5100 6550
	-1   0    0    1   
$EndComp
Wire Wire Line
	5350 6800 5450 6800
$Comp
L Connector:Conn_01x10_Male J_Selector1
U 1 1 6344B440
P 10750 4000
F 0 "J_Selector1" H 10950 4450 50  0000 C CNN
F 1 "Conn_01x10_Male" H 10800 4500 50  0001 C CNN
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
	9050 4950 8700 4950
Wire Wire Line
	8700 3900 10550 3900
Wire Wire Line
	8700 4300 10550 4300
$Comp
L Connector:Conn_01x02_Male J_Reset_Signal1
U 1 1 6377AE00
P 8850 2650
F 0 "J_Reset_Signal1" H 8800 2500 50  0000 C CNN
F 1 "Conn_01x02_Male" H 8958 2740 50  0001 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x02_P2.54mm_Vertical" H 8850 2650 50  0001 C CNN
F 3 "~" H 8850 2650 50  0001 C CNN
	1    8850 2650
	1    0    0    -1  
$EndComp
Wire Wire Line
	9050 2750 9150 2750
Wire Wire Line
	9150 2750 9150 3000
Connection ~ 9150 3000
Wire Wire Line
	9150 3000 2400 3000
Wire Wire Line
	9050 2650 9250 2650
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_3
U 1 1 6254DD77
P 1950 6250
F 0 "C1.0nF_3" H 2050 6100 50  0000 L CNN
F 1 "885012207033" H 2200 6424 50  0001 C CNN
F 2 "CAPC2012X90N" H 2300 6300 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 2300 6200 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 2300 6100 50  0001 L CNN "Description"
F 5 "0.9" H 2300 6000 50  0001 L CNN "Height"
F 6 "710-885012207033" H 2300 5900 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 2300 5800 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 2300 5700 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 2300 5600 50  0001 L CNN "Manufacturer_Part_Number"
	1    1950 6250
	-1   0    0    1   
$EndComp
Wire Wire Line
	8700 3700 10550 3700
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_8
U 1 1 62DF072A
P 9550 4950
F 0 "C1.0nF_8" H 9650 4800 50  0000 L CNN
F 1 "885012207033" H 9800 5124 50  0001 C CNN
F 2 "CAPC2012X90N" H 9900 5000 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 9900 4900 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 9900 4800 50  0001 L CNN "Description"
F 5 "0.9" H 9900 4700 50  0001 L CNN "Height"
F 6 "710-885012207033" H 9900 4600 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 9900 4500 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 9900 4400 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 9900 4300 50  0001 L CNN "Manufacturer_Part_Number"
	1    9550 4950
	-1   0    0    1   
$EndComp
Wire Wire Line
	9550 4950 9800 4950
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_7
U 1 1 6271AAC0
P 7650 4950
F 0 "C1.0nF_7" H 7750 4800 50  0000 L CNN
F 1 "885012207033" H 7900 5124 50  0001 C CNN
F 2 "CAPC2012X90N" H 8000 5000 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 8000 4900 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 8000 4800 50  0001 L CNN "Description"
F 5 "0.9" H 8000 4700 50  0001 L CNN "Height"
F 6 "710-885012207033" H 8000 4600 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 8000 4500 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 8000 4400 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 8000 4300 50  0001 L CNN "Manufacturer_Part_Number"
	1    7650 4950
	-1   0    0    1   
$EndComp
Wire Wire Line
	6450 4950 7150 4950
Wire Wire Line
	7650 4950 7900 4950
Wire Wire Line
	1050 750  1500 750 
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
Text Label 2600 6800 0    50   ~ 0
CO0
Text Label 2600 6900 0    50   ~ 0
CO1
Text Label 2600 7000 0    50   ~ 0
CO2
Text Label 2600 7100 0    50   ~ 0
CO3
Text Label 2000 4600 0    50   ~ 0
OR_Out
Text Label 4400 4200 2    50   ~ 0
AND_Out
Wire Wire Line
	5500 3450 5250 3450
Wire Wire Line
	4400 4200 3000 4200
Wire Wire Line
	3000 4200 3000 4800
Wire Wire Line
	3000 4800 2000 4800
Wire Wire Line
	9900 1500 9300 1500
Connection ~ 9300 1500
Wire Wire Line
	10400 1500 10900 1500
Connection ~ 10900 1500
Wire Wire Line
	10900 1500 10900 1200
Wire Wire Line
	3350 1900 2600 1900
$Comp
L Nexperia:74LVC2G32DC-Q100H LVC32
U 1 1 61CAC7F2
P 1000 4500
F 0 "LVC32" H 1500 4350 50  0000 C CNN
F 1 "74LVC2G32DC-Q100H" H 1500 4050 50  0000 C CNN
F 2 "SOP50P310X100-8N" H 1850 4600 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC2G32_Q100.pdf" H 1850 4500 50  0001 L CNN
F 4 "74LVC2G32-Q100 - Dual 2-input OR gate@en-us" H 1850 4400 50  0001 L CNN "Description"
F 5 "1" H 1850 4300 50  0001 L CNN "Height"
F 6 "771-74LVC2G32DCQ100H" H 1850 4200 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC2G32DC-Q100H?qs=fi7yB2oewZkpBkB%252Bo1Xtow%3D%3D" H 1850 4100 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 1850 4000 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC2G32DC-Q100H" H 1850 3900 50  0001 L CNN "Manufacturer_Part_Number"
	1    1000 4500
	1    0    0    -1  
$EndComp
Wire Wire Line
	5100 6550 5450 6550
Wire Wire Line
	5450 6550 5450 6800
Wire Wire Line
	4600 6550 3650 6550
Wire Wire Line
	1000 5550 1000 6700
Wire Wire Line
	2300 6250 1950 6250
Wire Wire Line
	2300 6250 2300 6400
Wire Wire Line
	1450 6250 800  6250
Wire Wire Line
	4650 4750 3650 4750
Wire Wire Line
	800  4150 800  4800
Wire Wire Line
	800  4800 1000 4800
Wire Wire Line
	800  4150 1250 4150
Wire Wire Line
	2200 4150 2200 4300
Wire Wire Line
	2000 4500 2200 4500
Wire Wire Line
	1750 4150 2200 4150
Wire Wire Line
	3650 6550 3650 7500
Wire Wire Line
	3850 4300 3850 3450
Wire Wire Line
	3850 4300 4400 4300
Wire Wire Line
	3850 3450 4750 3450
Wire Wire Line
	1100 5450 1100 6600
Wire Wire Line
	2100 4600 2100 5450
Wire Wire Line
	2100 5450 1100 5450
Wire Wire Line
	1000 4500 900  4500
Wire Wire Line
	5150 4750 5500 4750
$Comp
L power:PWR_FLAG #FLG0101
U 1 1 61CABBBC
P 1050 850
F 0 "#FLG0101" H 1050 925 50  0001 C CNN
F 1 "PWR_FLAG" V 1050 977 50  0000 L CNN
F 2 "" H 1050 850 50  0001 C CNN
F 3 "~" H 1050 850 50  0001 C CNN
	1    1050 850 
	0    -1   -1   0   
$EndComp
$Comp
L power:PWR_FLAG #FLG0102
U 1 1 61CACB41
P 1050 950
F 0 "#FLG0102" H 1050 1025 50  0001 C CNN
F 1 "PWR_FLAG" V 1050 1077 50  0000 L CNN
F 2 "" H 1050 950 50  0001 C CNN
F 3 "~" H 1050 950 50  0001 C CNN
	1    1050 950 
	0    -1   -1   0   
$EndComp
$Comp
L power:PWR_FLAG #FLG0103
U 1 1 61CD788B
P 1050 750
F 0 "#FLG0103" H 1050 825 50  0001 C CNN
F 1 "PWR_FLAG" V 1050 877 50  0000 L CNN
F 2 "" H 1050 750 50  0001 C CNN
F 3 "~" H 1050 750 50  0001 C CNN
	1    1050 750 
	0    -1   -1   0   
$EndComp
Wire Wire Line
	8800 5200 8700 5200
Connection ~ 8700 5200
Wire Wire Line
	8700 5200 8700 4950
Wire Wire Line
	9800 4950 9800 5200
Wire Wire Line
	6900 5200 6450 5200
Connection ~ 6450 5200
Wire Wire Line
	6450 5200 6450 4950
Wire Wire Line
	7900 4950 7900 5200
Wire Wire Line
	6000 6300 3550 6300
Wire Wire Line
	3550 6300 3550 5550
Connection ~ 6000 6300
Wire Wire Line
	3550 5550 1000 5550
$Comp
L Clock-rescue:885012207027-SamacSys_Parts C0.1nF_0
U 1 1 61CEFC2B
P 1650 1750
F 0 "C0.1nF_0" H 1750 1600 50  0000 L CNN
F 1 "885012207027" V 2000 1150 50  0001 L CNN
F 2 "CAPC2012X90N" H 2000 1800 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207027.pdf" H 2000 1700 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 100pF X7R0805101K016DFCT10000" H 2000 1600 50  0001 L CNN "Description"
F 5 "0.9" H 2000 1500 50  0001 L CNN "Height"
F 6 "710-885012207027" H 2000 1400 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207027?qs=0KOYDY2FL28IWYVDh0DkrQ%3D%3D" H 2000 1300 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 2000 1200 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207027" H 2000 1100 50  0001 L CNN "Manufacturer_Part_Number"
	1    1650 1750
	-1   0    0    1   
$EndComp
Wire Wire Line
	2200 1750 1650 1750
Connection ~ 2200 1750
Wire Wire Line
	2200 1750 2200 1900
Wire Wire Line
	1150 1750 800  1750
Connection ~ 800  1750
Wire Wire Line
	800  1750 800  2450
Wire Wire Line
	900  1900 900  2250
$Comp
L Wurth_Elektronik:885012207033 C1.0nF_11
U 1 1 6219E95F
P 1650 1450
F 0 "C1.0nF_11" H 1750 1300 50  0000 L CNN
F 1 "885012207033" H 1900 1624 50  0001 C CNN
F 2 "CAPC2012X90N" H 2000 1500 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207033.pdf" H 2000 1400 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 1000pF X7R0805102K016DFCT10000" H 2000 1300 50  0001 L CNN "Description"
F 5 "0.9" H 2000 1200 50  0001 L CNN "Height"
F 6 "710-885012207033" H 2000 1100 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207033?qs=0KOYDY2FL28ky2lrUPvQ%2Fw%3D%3D" H 2000 1000 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 2000 900 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207033" H 2000 800 50  0001 L CNN "Manufacturer_Part_Number"
	1    1650 1450
	-1   0    0    1   
$EndComp
Wire Wire Line
	2200 1450 1650 1450
Wire Wire Line
	2200 1450 2200 1750
Wire Wire Line
	1150 1450 800  1450
Wire Wire Line
	800  1450 800  1750
$Comp
L Wurth_Elektronik:885012207027 C0.1nF_1
U 1 1 61C612DC
P 4100 1650
F 0 "C0.1nF_1" H 4200 1500 50  0000 L CNN
F 1 "885012207027" V 4450 1050 50  0001 L CNN
F 2 "CAPC2012X90N" H 4450 1700 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207027.pdf" H 4450 1600 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 100pF X7R0805101K016DFCT10000" H 4450 1500 50  0001 L CNN "Description"
F 5 "0.9" H 4450 1400 50  0001 L CNN "Height"
F 6 "710-885012207027" H 4450 1300 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207027?qs=0KOYDY2FL28IWYVDh0DkrQ%3D%3D" H 4450 1200 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 4450 1100 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207027" H 4450 1000 50  0001 L CNN "Manufacturer_Part_Number"
	1    4100 1650
	-1   0    0    1   
$EndComp
Wire Wire Line
	4100 1650 4450 1650
Connection ~ 4450 1650
Wire Wire Line
	3600 1650 3150 1650
Connection ~ 3150 1650
Wire Wire Line
	6900 1400 6500 1400
Connection ~ 6900 1400
Wire Wire Line
	6900 1400 6900 1100
Wire Wire Line
	6900 1100 6500 1100
Wire Wire Line
	6000 1100 5300 1100
Wire Wire Line
	6000 1400 5300 1400
Connection ~ 5300 1400
Wire Wire Line
	5300 1400 5300 1100
Wire Wire Line
	5300 2450 5700 2450
Wire Wire Line
	4450 1350 4100 1350
Wire Wire Line
	4450 1350 4450 1650
Wire Wire Line
	3600 1350 3150 1350
Wire Wire Line
	8900 1750 9500 1750
Wire Wire Line
	8800 1750 8900 1750
Connection ~ 8900 1750
Wire Wire Line
	8900 1750 8900 1400
$Comp
L Connector:Conn_01x02_Male J_Reset_Switch1
U 1 1 6303295C
P 8000 1300
F 0 "J_Reset_Switch1" H 8050 1150 50  0000 C CNN
F 1 "Conn_01x02_Male" H 8050 1400 50  0001 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x02_P2.54mm_Vertical" H 8000 1300 50  0001 C CNN
F 3 "~" H 8000 1300 50  0001 C CNN
	1    8000 1300
	1    0    0    -1  
$EndComp
Wire Wire Line
	8900 1400 8200 1400
Wire Wire Line
	9400 2750 9250 2750
Wire Wire Line
	9250 2750 9250 2650
Connection ~ 9400 2750
Wire Wire Line
	9400 2350 9400 2750
$Comp
L Wurth_Elektronik:885012207027 C0.1nF_3
U 1 1 62E62CF1
P 10400 1200
F 0 "C0.1nF_3" H 10500 1050 50  0000 L CNN
F 1 "885012207027" V 10750 600 50  0001 L CNN
F 2 "CAPC2012X90N" H 10750 1250 50  0001 L CNN
F 3 "https://katalog.we-online.com/pbs/datasheet/885012207027.pdf" H 10750 1150 50  0001 L CNN
F 4 "Multilayer Ceramic Chip Capacitor WCAP-CSGP Series 0805 100pF X7R0805101K016DFCT10000" H 10750 1050 50  0001 L CNN "Description"
F 5 "0.9" H 10750 950 50  0001 L CNN "Height"
F 6 "710-885012207027" H 10750 850 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Wurth-Elektronik/885012207027?qs=0KOYDY2FL28IWYVDh0DkrQ%3D%3D" H 10750 750 50  0001 L CNN "Mouser Price/Stock"
F 8 "Wurth Elektronik" H 10750 650 50  0001 L CNN "Manufacturer_Name"
F 9 "885012207027" H 10750 550 50  0001 L CNN "Manufacturer_Part_Number"
	1    10400 1200
	-1   0    0    1   
$EndComp
Wire Wire Line
	10400 1200 10900 1200
Wire Wire Line
	9900 1200 9300 1200
Wire Wire Line
	9300 1200 9300 1500
Text Label 3150 1350 0    50   ~ 0
GND
Text Label 4450 1350 2    50   ~ 0
3.3V
Text Label 2200 1450 2    50   ~ 0
3.3V
Text Label 800  1450 0    50   ~ 0
GND
Text Label 5300 1100 0    50   ~ 0
GND
Text Label 6900 1100 2    50   ~ 0
3.3V
Text Label 7700 1050 0    50   ~ 0
GND
Text Label 9000 1050 2    50   ~ 0
5V
Wire Wire Line
	7700 1050 8900 1050
Wire Wire Line
	8900 1050 8900 1300
Text Label 10900 1200 2    50   ~ 0
3.3V
Text Label 9300 1200 0    50   ~ 0
GND
Wire Wire Line
	1050 950  1500 950 
Wire Wire Line
	1050 850  1500 850 
Wire Wire Line
	2000 950  2400 950 
Wire Wire Line
	3150 1350 3150 1650
Text Label 800  4150 0    50   ~ 0
GND
Text Label 2200 4150 2    50   ~ 0
3.3V
Wire Wire Line
	900  4500 900  4300
Wire Wire Line
	900  4300 2200 4300
Connection ~ 2200 4300
Wire Wire Line
	2200 4300 2200 4500
Wire Wire Line
	1000 4600 900  4600
Wire Wire Line
	900  4600 900  4500
Connection ~ 900  4500
Text Label 800  6250 0    50   ~ 0
GND
Text Label 2300 6250 2    50   ~ 0
3.3V
Wire Wire Line
	2300 6400 900  6400
Wire Wire Line
	900  6400 900  6800
Wire Wire Line
	900  6800 1200 6800
Wire Wire Line
	900  6800 900  6900
Wire Wire Line
	900  7100 1200 7100
Connection ~ 900  6800
Wire Wire Line
	1200 7000 900  7000
Connection ~ 900  7000
Wire Wire Line
	900  7000 900  7100
Wire Wire Line
	1200 6900 900  6900
Connection ~ 900  6900
Wire Wire Line
	900  6900 900  7000
$Comp
L Nexperia:74LVC163PW,118 LVC163
U 1 1 61C67470
P 1200 6600
F 0 "LVC163" H 1700 6250 50  0000 C CNN
F 1 "74LVC163PW,118" H 1700 5750 50  0000 C CNN
F 2 "SOP65P640X110-16N" H 2050 6700 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC163.pdf" H 2050 6600 50  0001 L CNN
F 4 "74LVC163 - Presettable synchronous 4-bit binary counter; synchronous reset@en-us" H 2050 6500 50  0001 L CNN "Description"
F 5 "1.1" H 2050 6400 50  0001 L CNN "Height"
F 6 "771-74LVC163PW-T" H 2050 6300 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC163PW118?qs=me8TqzrmIYUWRf9xrCNBjw%3D%3D" H 2050 6200 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 2050 6100 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC163PW,118" H 2050 6000 50  0001 L CNN "Manufacturer_Part_Number"
	1    1200 6600
	1    0    0    -1  
$EndComp
Wire Wire Line
	8800 1650 9000 1650
Wire Wire Line
	9000 1650 9000 1050
Text Label 8700 4950 0    50   ~ 0
GND
Text Label 9800 4950 2    50   ~ 0
3.3V
Text Label 7900 4950 2    50   ~ 0
3.3V
Text Label 6450 4950 0    50   ~ 0
GND
Wire Wire Line
	5500 5050 5500 4750
Text Label 5500 4750 2    50   ~ 0
3.3V
Text Label 3650 4750 0    50   ~ 0
GND
Text Label 3850 3450 0    50   ~ 0
GND
Wire Wire Line
	5600 3800 5600 3450
Text Label 5600 3450 0    50   ~ 0
3.3V
Text Label 5450 6550 2    50   ~ 0
3.3V
Text Label 3650 6550 0    50   ~ 0
GND
Wire Wire Line
	9400 2250 9400 1650
Wire Wire Line
	9400 1650 10900 1650
Connection ~ 10900 1650
Wire Wire Line
	10900 1650 10900 1500
Wire Wire Line
	5300 1400 5300 2450
Wire Wire Line
	5700 2150 5400 2150
Wire Wire Line
	5600 1850 5700 1850
Wire Wire Line
	5600 1850 5600 1650
Wire Wire Line
	9800 6100 9900 6100
Wire Wire Line
	9900 6100 9900 6400
Wire Wire Line
	6100 6400 9900 6400
Wire Wire Line
	7900 6100 8000 6100
Wire Wire Line
	8000 6100 8000 6300
Wire Wire Line
	6000 6300 8000 6300
Wire Wire Line
	7200 2900 7200 3300
Wire Wire Line
	7200 3300 6000 3300
Wire Wire Line
	6000 3300 6000 6300
$Comp
L Nexperia:74LVC138AD-Q100J LVC138
U 1 1 63767221
P 4350 6800
F 0 "LVC138" H 4850 6450 50  0000 C CNN
F 1 "74LVC138AD-Q100J" H 4850 5950 50  0000 C CNN
F 2 "SOIC127P600X175-16N" H 5200 6900 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC138A_Q100.pdf" H 5200 6800 50  0001 L CNN
F 4 "74LVC138A-Q100 - 3-to-8 line decoder/demultiplexer; inverting@en-us" H 5200 6700 50  0001 L CNN "Description"
F 5 "1.75" H 5200 6600 50  0001 L CNN "Height"
F 6 "771-74LVC138ADQ100J" H 5200 6500 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC138AD-Q100J?qs=fi7yB2oewZlqIVf8HNGjZw%3D%3D" H 5200 6400 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 5200 6300 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC138AD-Q100J" H 5200 6200 50  0001 L CNN "Manufacturer_Part_Number"
	1    4350 6800
	1    0    0    -1  
$EndComp
$Comp
L Toshiba:74LCX04FT_AE_ LCX4
U 1 1 637BD5FB
P 4400 5050
F 0 "LCX4" H 4900 5200 50  0000 C CNN
F 1 "74LCX04FT_AE_" H 4900 4300 50  0000 C CNN
F 2 "SOP65P640X120-14N" H 5250 5150 50  0001 L CNN
F 3 "https://toshiba.semicon-storage.com/info/docget.jsp?did=15379&prodName=74LCX04FT" H 5250 5050 50  0001 L CNN
F 4 "Inverters Low voltage CMOS Logic IC" H 5250 4950 50  0001 L CNN "Description"
F 5 "1.2" H 5250 4850 50  0001 L CNN "Height"
F 6 "757-74LCX04FTAE" H 5250 4750 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Toshiba/74LCX04FTAE?qs=cW4DzVrAanMxusL1byWNzQ%3D%3D" H 5250 4650 50  0001 L CNN "Mouser Price/Stock"
F 8 "Toshiba" H 5250 4550 50  0001 L CNN "Manufacturer_Name"
F 9 "74LCX04FT(AE)" H 5250 4450 50  0001 L CNN "Manufacturer_Part_Number"
	1    4400 5050
	1    0    0    -1  
$EndComp
Wire Wire Line
	1000 2450 800  2450
Wire Wire Line
	2100 2450 2600 2450
Wire Wire Line
	2600 1900 2600 2450
$Comp
L Renesas:XLH735100.000000I X100Mhz1
U 1 1 638093CA
P 1000 2250
F 0 "X100Mhz1" H 1550 2423 50  0000 C CNN
F 1 "XLH735100.000000I" H 1550 2424 50  0001 C CNN
F 2 "XLH735100000000I" H 1950 2350 50  0001 L CNN
F 3 "https://www.renesas.com/us/en/document/dst/xl-family-low-phase-noise-quartz-based-pll-oscillators-datasheet" H 1950 2250 50  0001 L CNN
F 4 "SMD Crystal Oscillator 100MHz 3.3V 15pF 4-Pin CLCC Cut Tape" H 1950 2150 50  0001 L CNN "Description"
F 5 "1.45" H 1950 2050 50  0001 L CNN "Height"
F 6 "972-XLH735100000000I" H 1950 1950 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Renesas-IDT/XLH735100000000I?qs=itIaUkCn8qAdqSTL6ktHsw%3D%3D" H 1950 1850 50  0001 L CNN "Mouser Price/Stock"
F 8 "Renesas Electronics" H 1950 1750 50  0001 L CNN "Manufacturer_Name"
F 9 "XLH735100.000000I" H 1950 1650 50  0001 L CNN "Manufacturer_Part_Number"
	1    1000 2250
	1    0    0    -1  
$EndComp
NoConn ~ 2100 2350
NoConn ~ 1000 2350
Wire Wire Line
	2200 1900 2200 2250
Wire Wire Line
	2200 2250 2100 2250
Connection ~ 2200 1900
Wire Wire Line
	900  1900 2200 1900
Text Label 2200 2450 0    50   ~ 0
X100
Text Label 3250 2400 2    50   ~ 0
IX100
Connection ~ 7450 2150
Wire Wire Line
	7300 2050 7300 2450
Text Label 9150 1750 2    50   ~ 0
RSTB
Entry Wire Line
	8100 5200 8200 5100
Wire Wire Line
	7900 5300 8100 5300
Wire Wire Line
	7900 5400 8200 5400
Wire Wire Line
	8100 5200 8100 5300
Connection ~ 8100 5300
Wire Wire Line
	8100 5300 8800 5300
Wire Wire Line
	7900 5500 8300 5500
Wire Wire Line
	7900 5600 8400 5600
Wire Wire Line
	7900 5700 8500 5700
Entry Wire Line
	8200 5200 8300 5100
Entry Wire Line
	8300 5200 8400 5100
Entry Wire Line
	8400 5200 8500 5100
Entry Wire Line
	8500 5200 8600 5100
Wire Wire Line
	8200 5200 8200 5400
Connection ~ 8200 5400
Wire Wire Line
	8200 5400 8800 5400
Wire Wire Line
	8300 5200 8300 5500
Connection ~ 8300 5500
Wire Wire Line
	8300 5500 8800 5500
Wire Wire Line
	8400 5200 8400 5600
Connection ~ 8400 5600
Wire Wire Line
	8400 5600 8800 5600
Wire Wire Line
	8500 5200 8500 5700
Connection ~ 8500 5700
Wire Wire Line
	8500 5700 8800 5700
Entry Wire Line
	8600 3600 8700 3500
Entry Wire Line
	8600 3800 8700 3700
Entry Wire Line
	8600 4000 8700 3900
Entry Wire Line
	8600 4200 8700 4100
Entry Wire Line
	8600 4400 8700 4300
Text Label 7900 5300 0    50   ~ 0
SEL0
Text Label 7900 5400 0    50   ~ 0
SEL2
Text Label 7900 5500 0    50   ~ 0
SEL4
Text Label 7900 5600 0    50   ~ 0
SEL6
Text Label 7900 5700 0    50   ~ 0
SEL8
Wire Wire Line
	6450 5200 6450 6100
Wire Wire Line
	5600 7600 5350 7600
Wire Wire Line
	5350 7500 5600 7500
Wire Wire Line
	5350 7400 5600 7400
Wire Wire Line
	5600 7300 5350 7300
Wire Wire Line
	5600 7200 5350 7200
Wire Wire Line
	5600 7100 5350 7100
Wire Wire Line
	5600 7000 5350 7000
Entry Wire Line
	5600 7600 5700 7500
Entry Wire Line
	5600 7500 5700 7400
Entry Wire Line
	5600 7400 5700 7300
Entry Wire Line
	5600 7300 5700 7200
Entry Wire Line
	5600 7200 5700 7100
Entry Wire Line
	5600 7100 5700 7000
Entry Wire Line
	5600 7000 5700 6900
Entry Wire Line
	5600 6900 5700 6800
Wire Wire Line
	5350 6900 5600 6900
Wire Wire Line
	6450 6100 6900 6100
Connection ~ 9500 1750
Wire Wire Line
	6900 1850 6900 2250
Wire Wire Line
	6800 1950 7000 1950
Wire Wire Line
	8900 1950 8900 1750
Wire Wire Line
	6900 1400 6900 1550
Wire Wire Line
	5600 1650 7000 1650
Wire Wire Line
	7000 1650 7000 1950
Connection ~ 7000 1950
Wire Wire Line
	7000 1950 8900 1950
Wire Wire Line
	5400 2150 5400 1550
Wire Wire Line
	5400 1550 6900 1550
Connection ~ 6900 1550
Wire Wire Line
	6900 1550 6900 1850
Wire Wire Line
	7450 2150 9600 2150
Text Label 4250 5150 0    50   ~ 0
ICO0
Text Label 4250 5350 0    50   ~ 0
ICO1
Text Label 4250 5550 0    50   ~ 0
ICO3
Text Label 5100 2250 0    50   ~ 0
HClock
Text Label 5100 2350 0    50   ~ 0
HClock_B
Wire Wire Line
	8600 1300 8900 1300
Wire Wire Line
	8300 1300 8200 1300
$Comp
L Device:R R1
U 1 1 62BADAA7
P 8450 1300
F 0 "R1" V 8350 1300 50  0000 C CNN
F 1 "R" V 8450 1300 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P7.62mm_Horizontal" V 8380 1300 50  0001 C CNN
F 3 "~" H 8450 1300 50  0001 C CNN
	1    8450 1300
	0    1    1    0   
$EndComp
$Comp
L Connector:Conn_01x05_Male J_Multiplexer2
U 1 1 61D701C1
P 6150 7100
F 0 "J_Multiplexer2" H 6500 7350 50  0000 R CNN
F 1 "Conn_01x05_Male" H 6122 7123 50  0001 R CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x05_P2.54mm_Vertical" H 6150 7100 50  0001 C CNN
F 3 "~" H 6150 7100 50  0001 C CNN
	1    6150 7100
	-1   0    0    1   
$EndComp
Entry Wire Line
	5700 6800 5800 6900
Entry Wire Line
	5700 6900 5800 7000
Entry Wire Line
	5700 7000 5800 7100
Entry Wire Line
	5700 7100 5800 7200
Entry Wire Line
	5700 7200 5800 7300
Wire Wire Line
	5800 7300 5950 7300
Wire Wire Line
	5800 7200 5950 7200
Wire Wire Line
	5800 7100 5950 7100
Wire Wire Line
	5800 7000 5950 7000
Wire Wire Line
	5800 6900 5950 6900
Text Label 5850 6900 0    50   ~ 0
M0
Text Label 5850 7000 0    50   ~ 0
M1
Text Label 5850 7100 0    50   ~ 0
M2
Text Label 5850 7200 0    50   ~ 0
M3
Text Label 5850 7300 0    50   ~ 0
M4
$Comp
L Connector:Conn_01x04_Male J_Multiplexer1
U 1 1 61F4632A
P 3400 6500
F 0 "J_Multiplexer1" H 3750 6750 50  0000 R CNN
F 1 "Conn_01x05_Male" H 3372 6523 50  0001 R CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x04_P2.54mm_Vertical" H 3400 6500 50  0001 C CNN
F 3 "~" H 3400 6500 50  0001 C CNN
	1    3400 6500
	-1   0    0    1   
$EndComp
Wire Wire Line
	3200 6600 3200 7100
Connection ~ 3200 7100
Wire Wire Line
	3200 7100 4150 7100
Wire Wire Line
	3200 6500 3100 6500
Wire Wire Line
	3200 6400 3000 6400
Wire Wire Line
	3000 6400 3000 6900
Connection ~ 3000 6900
Wire Wire Line
	3000 6900 3850 6900
Wire Wire Line
	2200 7100 3200 7100
Wire Wire Line
	3100 6500 3100 7000
Connection ~ 3100 7000
Wire Wire Line
	3100 7000 3950 7000
Wire Wire Line
	3200 6300 2900 6300
Wire Wire Line
	2200 6900 3000 6900
Wire Wire Line
	2900 6300 2900 6800
Connection ~ 2900 6800
Wire Wire Line
	2900 6800 3750 6800
Wire Wire Line
	2300 6400 2300 6600
Wire Wire Line
	2300 6600 2200 6600
Connection ~ 2300 6400
Wire Wire Line
	2300 6600 2300 7200
Wire Wire Line
	2300 7200 2200 7200
Connection ~ 2300 6600
Wire Wire Line
	2300 7200 2300 7300
Wire Wire Line
	2300 7300 2200 7300
Connection ~ 2300 7200
Wire Wire Line
	2200 7000 3100 7000
Wire Wire Line
	2200 6800 2900 6800
Wire Bus Line
	8600 3600 8600 5100
Wire Bus Line
	8200 5100 8600 5100
Wire Bus Line
	5700 5400 5700 7500
Wire Wire Line
	2400 7300 4350 7300
$EndSCHEMATC
