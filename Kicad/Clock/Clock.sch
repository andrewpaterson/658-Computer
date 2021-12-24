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
L SamacSys_Parts:74LVCU04APW,118 LVCU4
U 1 1 61C44AF2
P 5050 2500
F 0 "LVCU4" H 5550 2765 50  0000 C CNN
F 1 "74LVCU04APW,118" H 5550 2674 50  0000 C CNN
F 2 "SOP65P640X110-14N" H 5900 2600 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVCU04A.pdf" H 5900 2500 50  0001 L CNN
F 4 "74LVCU04A - Hex unbuffered inverter@en-us" H 5900 2400 50  0001 L CNN "Description"
F 5 "1.1" H 5900 2300 50  0001 L CNN "Height"
F 6 "771-LVCU04APW118" H 5900 2200 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVCU04APW118?qs=me8TqzrmIYXibrDyFt%252BDyw%3D%3D" H 5900 2100 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 5900 2000 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVCU04APW,118" H 5900 1900 50  0001 L CNN "Manufacturer_Part_Number"
	1    5050 2500
	1    0    0    -1  
$EndComp
$Comp
L Oscillator:OH300 X100Mhz
U 1 1 61C47738
P 3600 2500
F 0 "X100Mhz" H 3650 2750 50  0000 L CNN
F 1 "XLH735100" H 3100 2250 50  0000 L CNN
F 2 "Oscillator:Oscillator_SMD_IDT_JU6-6_7.0x5.0mm_P2.54mm" H 3600 2150 50  0001 C CNN
F 3 "http://www.conwin.com/datasheets/cx/cx282.pdf" H 3400 2600 50  0001 C CNN
	1    3600 2500
	1    0    0    -1  
$EndComp
$Comp
L Device:C C3
U 1 1 61C47FD9
P 4450 2250
F 0 "C3" H 4565 2296 50  0000 L CNN
F 1 "C1nF" H 4565 2205 50  0000 L CNN
F 2 "Capacitor_THT:C_Disc_D4.7mm_W2.5mm_P5.00mm" H 4488 2100 50  0001 C CNN
F 3 "~" H 4450 2250 50  0001 C CNN
	1    4450 2250
	1    0    0    -1  
$EndComp
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
	6150 2500 6050 2500
Wire Wire Line
	6150 2500 6150 2600
Wire Wire Line
	6150 2600 6050 2600
Connection ~ 6150 2500
Wire Wire Line
	6050 2700 6150 2700
Wire Wire Line
	6150 2700 6150 2800
Wire Wire Line
	6150 2800 6050 2800
Wire Wire Line
	6050 2900 6150 2900
Wire Wire Line
	6150 2900 6150 3000
Wire Wire Line
	6150 3000 6050 3000
Wire Wire Line
	6150 1900 6150 2500
Wire Wire Line
	3600 2800 3600 2850
$Comp
L Device:C C1
U 1 1 61C5537E
P 2850 2250
F 0 "C1" H 2965 2296 50  0000 L CNN
F 1 "C0.1nF" H 2965 2205 50  0000 L CNN
F 2 "Capacitor_THT:C_Disc_D4.7mm_W2.5mm_P5.00mm" H 2888 2100 50  0001 C CNN
F 3 "~" H 2850 2250 50  0001 C CNN
	1    2850 2250
	1    0    0    -1  
$EndComp
Connection ~ 3600 2850
Wire Wire Line
	3600 2850 3600 3550
Wire Wire Line
	5050 2600 4950 2600
Wire Wire Line
	4950 2600 4950 2700
Wire Wire Line
	4950 2700 5050 2700
Wire Wire Line
	5050 2800 4950 2800
Wire Wire Line
	4950 2800 4950 2900
Wire Wire Line
	4950 2900 5050 2900
Wire Wire Line
	4450 2100 4450 1900
Connection ~ 4450 1900
Wire Wire Line
	3600 1900 3600 2200
Wire Wire Line
	2850 2100 2850 1900
Wire Wire Line
	3600 1900 4450 1900
Connection ~ 3600 1900
Wire Wire Line
	3600 3550 4450 3550
Connection ~ 3600 3550
Connection ~ 4450 3550
Wire Wire Line
	2850 2850 3600 2850
Wire Wire Line
	2850 1900 3100 1900
Connection ~ 2850 1900
Wire Wire Line
	4000 2500 4350 2500
Wire Wire Line
	4450 3550 5900 3550
Wire Wire Line
	5050 3000 5000 3000
Wire Wire Line
	5000 3000 5000 4600
Wire Wire Line
	800  3550 1200 3550
Wire Wire Line
	4450 4500 4650 4500
Wire Wire Line
	4650 4500 4650 4900
Wire Wire Line
	4650 4900 4450 4900
Wire Wire Line
	1050 1900 1050 3850
Connection ~ 1050 1900
Wire Wire Line
	1050 1900 800  1900
Wire Wire Line
	1200 3550 1200 5400
Connection ~ 1200 3550
Wire Wire Line
	4450 4300 4550 4300
Wire Wire Line
	4550 4300 4550 3850
Connection ~ 4550 3850
Wire Wire Line
	4550 3850 5900 3850
$Comp
L Device:C C2
U 1 1 61C798B2
P 2950 4200
F 0 "C2" H 3065 4246 50  0000 L CNN
F 1 "C1nF" H 3065 4155 50  0000 L CNN
F 2 "Capacitor_THT:C_Disc_D4.7mm_W2.5mm_P5.00mm" H 2988 4050 50  0001 C CNN
F 3 "~" H 2950 4200 50  0001 C CNN
	1    2950 4200
	1    0    0    -1  
$EndComp
Wire Wire Line
	2950 4050 2950 3850
Wire Wire Line
	1200 5400 2950 5400
Wire Wire Line
	2950 4900 3350 4900
Wire Wire Line
	2950 4350 2950 4900
Connection ~ 2950 5400
Wire Wire Line
	2950 5400 5900 5400
Connection ~ 2950 4900
Wire Wire Line
	2950 4900 2950 5400
Wire Wire Line
	4550 4300 4550 4400
Wire Wire Line
	4550 4400 4450 4400
Connection ~ 4550 4300
Wire Wire Line
	4550 4400 4550 4700
Wire Wire Line
	4550 4700 4450 4700
Connection ~ 4550 4400
Wire Wire Line
	4450 4800 4550 4800
Wire Wire Line
	4550 4800 4550 5100
Wire Wire Line
	4550 5100 3250 5100
Wire Wire Line
	3250 5100 3250 4500
Wire Wire Line
	3250 4500 3350 4500
Wire Wire Line
	3350 4800 3150 4800
Wire Wire Line
	3150 4800 3150 4400
Wire Wire Line
	3150 4400 3350 4400
Wire Wire Line
	1050 3850 2950 3850
Wire Wire Line
	3350 4300 3050 4300
Wire Wire Line
	3050 4300 3050 3850
Connection ~ 3050 3850
Wire Wire Line
	3050 3850 4550 3850
Wire Wire Line
	3050 4300 3050 4600
Wire Wire Line
	3050 4600 3350 4600
Connection ~ 3050 4300
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
P 2450 4700
F 0 "J1" H 2558 4881 50  0000 C CNN
F 1 "Conn_01x02_Male" H 2558 4790 50  0000 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x02_P2.54mm_Vertical" H 2450 4700 50  0001 C CNN
F 3 "~" H 2450 4700 50  0001 C CNN
	1    2450 4700
	1    0    0    -1  
$EndComp
$Comp
L Connector:Conn_01x04_Male J2
U 1 1 61C9CBE6
P 4100 2800
F 0 "J2" H 4208 3081 50  0000 C CNN
F 1 "Conn_01x04_Male" H 4000 2500 50  0000 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x04_P2.54mm_Vertical" H 4100 2800 50  0001 C CNN
F 3 "~" H 4100 2800 50  0001 C CNN
	1    4100 2800
	1    0    0    -1  
$EndComp
Wire Wire Line
	2650 4700 3350 4700
Wire Wire Line
	3150 4800 2650 4800
Connection ~ 3150 4800
Connection ~ 5000 3000
Wire Wire Line
	4950 2800 4850 2800
Wire Wire Line
	4850 2800 4850 2900
Connection ~ 4950 2800
Wire Wire Line
	4300 2700 4350 2700
Wire Wire Line
	4350 2700 4350 2500
Wire Wire Line
	4950 2600 4750 2600
Wire Wire Line
	4750 2600 4750 2800
Connection ~ 4950 2600
Wire Wire Line
	4450 2400 4450 3100
Wire Wire Line
	4450 1900 6150 1900
Wire Wire Line
	4300 2800 4750 2800
Wire Wire Line
	4300 2900 4850 2900
Wire Wire Line
	4300 3000 5000 3000
Wire Wire Line
	4450 3100 5050 3100
Connection ~ 4450 3100
Wire Wire Line
	4450 3100 4450 3550
Wire Wire Line
	4350 2500 5050 2500
Connection ~ 4350 2500
Wire Wire Line
	4450 4600 5000 4600
Wire Wire Line
	2850 2400 2850 2850
Wire Wire Line
	3200 2500 3100 2500
Wire Wire Line
	3100 2500 3100 1900
Connection ~ 3100 1900
Wire Wire Line
	3100 1900 3600 1900
$Comp
L power:+5V #PWR?
U 1 1 61C5CAF2
P 1250 750
F 0 "#PWR?" H 1250 600 50  0001 C CNN
F 1 "+5V" H 1265 923 50  0000 C CNN
F 2 "" H 1250 750 50  0001 C CNN
F 3 "" H 1250 750 50  0001 C CNN
	1    1250 750 
	1    0    0    -1  
$EndComp
$Comp
L Connector_Generic:Conn_02x03_Odd_Even J?
U 1 1 61C5DE69
P 1700 850
F 0 "J?" H 1750 1167 50  0000 C CNN
F 1 "Conn_02x03" H 1750 600 50  0000 C CNN
F 2 "" H 1700 850 50  0001 C CNN
F 3 "~" H 1700 850 50  0001 C CNN
	1    1700 850 
	1    0    0    -1  
$EndComp
$Comp
L Device:CP C?
U 1 1 61C5F212
P 2950 800
F 0 "C?" H 3068 846 50  0000 L CNN
F 1 "CP1000uF" H 2750 550 50  0000 L CNN
F 2 "Capacitor_THT:CP_Radial_D7.5mm_P2.50mm" H 2988 650 50  0001 C CNN
F 3 "~" H 2950 800 50  0001 C CNN
	1    2950 800 
	1    0    0    -1  
$EndComp
Wire Wire Line
	1500 750  1250 750 
Wire Wire Line
	1500 850  1050 850 
Wire Wire Line
	1050 950  1500 950 
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
	2950 550  2950 650 
Wire Wire Line
	2400 650  2650 650 
Connection ~ 2400 650 
Wire Wire Line
	2150 550  2950 550 
Wire Wire Line
	2400 950  2950 950 
Connection ~ 2400 950 
Wire Wire Line
	2950 950  3200 950 
Wire Wire Line
	3200 950  3200 1200
Connection ~ 2950 950 
Wire Wire Line
	2650 1300 550  1300
Wire Wire Line
	2650 650  2650 1300
Wire Wire Line
	2650 1300 10600 1300
Connection ~ 2650 1300
Wire Wire Line
	550  1200 3200 1200
Wire Wire Line
	2100 950  2100 1400
Wire Wire Line
	2100 1400 550  1400
Connection ~ 2100 950 
Wire Wire Line
	2100 950  2400 950 
Wire Wire Line
	2100 1400 10600 1400
Connection ~ 2100 1400
Connection ~ 2950 3850
Wire Wire Line
	2950 3850 3050 3850
Wire Wire Line
	1050 1900 2850 1900
Wire Wire Line
	1200 3550 3600 3550
$Comp
L SamacSys_Parts:74LVC74APW,118 LVC74
U 1 1 61C454FF
P 3350 4300
F 0 "LVC74" H 3900 4565 50  0000 C CNN
F 1 "74LVC74APW,118" H 3900 4474 50  0000 C CNN
F 2 "SOP65P640X110-14N" H 4300 4400 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC74A.pdf" H 4300 4300 50  0001 L CNN
F 4 "74LVC74A - Dual D-type flip-flop with set and reset; positive-edge trigger@en-us" H 4300 4200 50  0001 L CNN "Description"
F 5 "1.1" H 4300 4100 50  0001 L CNN "Height"
F 6 "771-74LVC74APW-T" H 4300 4000 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC74APW118?qs=me8TqzrmIYVtXwVfet0lzw%3D%3D" H 4300 3900 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 4300 3800 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC74APW,118" H 4300 3700 50  0001 L CNN "Manufacturer_Part_Number"
	1    3350 4300
	1    0    0    -1  
$EndComp
$EndSCHEMATC
