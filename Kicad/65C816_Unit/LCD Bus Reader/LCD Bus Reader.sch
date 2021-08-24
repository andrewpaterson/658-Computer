EESchema Schematic File Version 4
EELAYER 30 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 1 1
Title "Bus Analyser"
Date "2021-08-23"
Rev "V0.1"
Comp ""
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
$Comp
L Device:D_Zener D2
U 1 1 611FC86D
P 9900 1150
F 0 "D2" V 9946 1070 50  0000 R CNN
F 1 "D_Zener" V 9855 1070 50  0001 R CNN
F 2 "Diode_THT:D_A-405_P7.62mm_Horizontal" H 9900 1150 50  0001 C CNN
F 3 "~" H 9900 1150 50  0001 C CNN
	1    9900 1150
	0    1    1    0   
$EndComp
$Comp
L Device:R R1
U 1 1 611FD853
P 9550 1150
F 0 "R1" V 9550 1150 50  0000 C CNN
F 1 "R" V 9434 1150 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 9480 1150 50  0001 C CNN
F 3 "~" H 9550 1150 50  0001 C CNN
	1    9550 1150
	1    0    0    -1  
$EndComp
Text Label 8400 700  0    50   ~ 0
0V
$Comp
L Device:D_Zener D1
U 1 1 611FCE23
P 9700 1450
F 0 "D1" H 9746 1370 50  0000 R CNN
F 1 "D_Zener" V 9655 1370 50  0001 R CNN
F 2 "Diode_THT:D_A-405_P7.62mm_Horizontal" H 9700 1450 50  0001 C CNN
F 3 "~" H 9700 1450 50  0001 C CNN
	1    9700 1450
	-1   0    0    1   
$EndComp
Wire Wire Line
	10300 1100 10300 700 
Connection ~ 10300 700 
Text Label 8400 800  0    50   ~ 0
12V
Text Label 8400 900  0    50   ~ 0
5V
Wire Wire Line
	9900 700  9900 1000
Connection ~ 9900 700 
Wire Wire Line
	9900 700  10300 700 
Wire Wire Line
	9900 1300 9900 1450
Wire Wire Line
	9900 1450 9850 1450
Wire Wire Line
	9550 1450 9550 1300
Wire Wire Line
	9550 1000 9550 900 
Connection ~ 9550 900 
Text Label 8300 2050 0    50   ~ 0
5V
Text Label 8300 1950 0    50   ~ 0
1.6V
Wire Wire Line
	9050 900  9050 2050
Connection ~ 9050 900 
Wire Wire Line
	9050 900  9550 900 
$Comp
L Display_Character:RC1602A U2
U 1 1 612226BC
P 5700 1350
F 0 "U2" V 5700 2094 50  0000 L CNN
F 1 "RC1602A" V 5745 2094 50  0001 L CNN
F 2 "Microtips LCD:NMTC-S16205" H 5800 550 50  0001 C CNN
F 3 "http://www.raystar-optronics.com/down.php?ProID=18" H 5800 1250 50  0001 C CNN
	1    5700 1350
	0    -1   -1   0   
$EndComp
Wire Wire Line
	5000 1350 4950 1350
$Comp
L Device:C C1
U 1 1 6127027D
P 550 3150
F 0 "C1" V 390 3150 50  0000 C CNN
F 1 "C" V 389 3150 50  0001 C CNN
F 2 "Capacitor_THT:C_Disc_D3.8mm_W2.6mm_P2.50mm" H 588 3000 50  0001 C CNN
F 3 "~" H 550 3150 50  0001 C CNN
	1    550  3150
	-1   0    0    1   
$EndComp
Wire Wire Line
	4950 1350 4950 750 
Wire Wire Line
	6450 750  6450 1350
Wire Wire Line
	6450 1350 6400 1350
Wire Wire Line
	550  3300 700  3300
NoConn ~ 950  2700
NoConn ~ 950  2800
NoConn ~ 950  2900
NoConn ~ 950  3200
NoConn ~ 6000 950 
NoConn ~ 5900 950 
$Comp
L power:PWR_FLAG #FLG0102
U 1 1 612CF214
P 10750 800
F 0 "#FLG0102" H 10750 875 50  0001 C CNN
F 1 "PWR_FLAG" H 10750 973 50  0000 C CNN
F 2 "" H 10750 800 50  0001 C CNN
F 3 "~" H 10750 800 50  0001 C CNN
	1    10750 800 
	1    0    0    -1  
$EndComp
$Comp
L Device:C C2
U 1 1 6126EA4F
P 5700 750
F 0 "C2" V 5540 750 50  0000 C CNN
F 1 "C" V 5539 750 50  0001 C CNN
F 2 "Capacitor_THT:C_Disc_D3.8mm_W2.6mm_P2.50mm" H 5738 600 50  0001 C CNN
F 3 "~" H 5700 750 50  0001 C CNN
	1    5700 750 
	0    -1   -1   0   
$EndComp
$Comp
L Connector:Conn_01x02_Male J1
U 1 1 612D58FF
P 11100 800
F 0 "J1" V 11162 844 50  0000 L CNN
F 1 "Conn_01x02_Male" V 11253 844 50  0000 L CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x02_P2.54mm_Vertical" H 11100 800 50  0001 C CNN
F 3 "~" H 11100 800 50  0001 C CNN
	1    11100 800 
	-1   0    0    1   
$EndComp
Wire Wire Line
	10300 700  10850 700 
Wire Wire Line
	10900 700  10850 700 
Connection ~ 10850 700 
Wire Wire Line
	8550 1850 8550 700 
Wire Wire Line
	8600 1850 8550 1850
$Comp
L Device:R_POT RV1
U 1 1 612084CC
P 8750 1850
F 0 "RV1" V 8750 1850 50  0000 C CNN
F 1 "R_POT" V 8544 1850 50  0001 C CNN
F 2 "Potentiometer_THT:Potentiometer_Omeg_PC16BU_Vertical" H 8750 1850 50  0001 C CNN
F 3 "~" H 8750 1850 50  0001 C CNN
	1    8750 1850
	0    -1   -1   0   
$EndComp
Wire Wire Line
	7700 900  7950 900 
Connection ~ 8550 700 
Wire Wire Line
	8550 700  9900 700 
Wire Wire Line
	7700 700  8300 700 
Wire Wire Line
	9550 900  10000 900 
Wire Wire Line
	10000 900  10000 1400
Wire Wire Line
	10600 800  7700 800 
Connection ~ 10600 800 
Wire Wire Line
	10600 1400 10600 800 
$Comp
L power:+12V #PWR0102
U 1 1 6130DB6A
P 10750 800
F 0 "#PWR0102" H 10750 650 50  0001 C CNN
F 1 "+12V" H 10765 973 50  0000 C CNN
F 2 "" H 10750 800 50  0001 C CNN
F 3 "" H 10750 800 50  0001 C CNN
	1    10750 800 
	-1   0    0    1   
$EndComp
$Comp
L power:PWR_FLAG #FLG0103
U 1 1 6130F3B5
P 8750 1650
F 0 "#FLG0103" H 8750 1725 50  0001 C CNN
F 1 "PWR_FLAG" H 8750 1823 50  0000 C CNN
F 2 "" H 8750 1650 50  0001 C CNN
F 3 "~" H 8750 1650 50  0001 C CNN
	1    8750 1650
	1    0    0    -1  
$EndComp
$Comp
L power:PWR_FLAG #FLG0101
U 1 1 61315B8A
P 10850 700
F 0 "#FLG0101" H 10850 775 50  0001 C CNN
F 1 "PWR_FLAG" H 10850 873 50  0000 C CNN
F 2 "" H 10850 700 50  0001 C CNN
F 3 "~" H 10850 700 50  0001 C CNN
	1    10850 700 
	1    0    0    -1  
$EndComp
$Comp
L power:PWR_FLAG #FLG0104
U 1 1 61315E1C
P 8250 1950
F 0 "#FLG0104" H 8250 2025 50  0001 C CNN
F 1 "PWR_FLAG" H 8250 2123 50  0000 C CNN
F 2 "" H 8250 1950 50  0001 C CNN
F 3 "~" H 8250 1950 50  0001 C CNN
	1    8250 1950
	1    0    0    -1  
$EndComp
$Comp
L Regulator_Linear:L7805 U3
U 1 1 61326C9A
P 10300 1400
F 0 "U3" H 10300 1550 50  0000 C CNN
F 1 "L7805" H 10300 1640 50  0001 C CNN
F 2 "Package_TO_SOT_THT:TO-220-3_Vertical" H 10325 1250 50  0001 L CIN
F 3 "http://www.st.com/content/ccc/resource/technical/document/datasheet/41/4f/b3/b0/12/d4/47/88/CD00000444.pdf/files/CD00000444.pdf/jcr:content/translations/en.CD00000444.pdf" H 10300 1350 50  0001 C CNN
	1    10300 1400
	-1   0    0    1   
$EndComp
$Comp
L Device:LED D3
U 1 1 61241F66
P 8100 1200
F 0 "D3" H 8093 945 50  0000 C CNN
F 1 "LED" H 8093 1036 50  0000 C CNN
F 2 "LED_THT:LED_D5.0mm" H 8100 1200 50  0001 C CNN
F 3 "~" H 8100 1200 50  0001 C CNN
	1    8100 1200
	-1   0    0    1   
$EndComp
$Comp
L Device:R R2
U 1 1 6125C63F
P 7950 1050
F 0 "R2" V 7950 1050 50  0000 C CNN
F 1 "R" V 7834 1050 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 7880 1050 50  0001 C CNN
F 3 "~" H 7950 1050 50  0001 C CNN
	1    7950 1050
	1    0    0    -1  
$EndComp
Connection ~ 7950 900 
Wire Wire Line
	8250 1200 8300 1200
Wire Wire Line
	8300 1200 8300 700 
Connection ~ 8300 700 
Wire Wire Line
	8300 700  8550 700 
$Comp
L 74xx:74LS138 U5
U 1 1 61290E85
P 3850 2450
F 0 "U5" H 3850 1569 50  0000 C CNN
F 1 "74LS138" H 3850 1660 50  0000 C CNN
F 2 "Package_SO:SO-16_3.9x9.9mm_P1.27mm" H 3850 2450 50  0001 C CNN
F 3 "http://www.ti.com/lit/gpn/sn74LS138" H 3850 2450 50  0001 C CNN
	1    3850 2450
	-1   0    0    1   
$EndComp
$Comp
L 74xx:74LS373 U4
U 1 1 61291DCE
P 3400 4550
F 0 "U4" V 3400 4600 50  0000 C CNN
F 1 "74LS373" V 3500 4600 50  0000 C CNN
F 2 "Package_SO:SO-20_12.8x7.5mm_P1.27mm" H 3400 4550 50  0001 C CNN
F 3 "http://www.ti.com/lit/gpn/sn74LS373" H 3400 4550 50  0001 C CNN
	1    3400 4550
	0    -1   -1   0   
$EndComp
Wire Wire Line
	900  3100 950  3100
Wire Wire Line
	1750 1100 1750 1000
Wire Wire Line
	1650 1100 1650 1000
Wire Wire Line
	1550 1100 1550 1000
$Comp
L Connector:Conn_01x03_Male J3
U 1 1 6130475A
P 1650 800
F 0 "J3" H 1758 1081 50  0000 C CNN
F 1 "Conn_01x03_Male" H 1758 990 50  0000 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x03_P2.54mm_Vertical" H 1650 800 50  0001 C CNN
F 3 "~" H 1650 800 50  0001 C CNN
	1    1650 800 
	0    1    1    0   
$EndComp
NoConn ~ 2350 2000
NoConn ~ 2350 1500
NoConn ~ 950  2200
$Comp
L MCU_RaspberryPi_and_Boards:Pico U1
U 1 1 612261DA
P 1650 2250
F 0 "U1" H 1600 3000 50  0000 L CNN
F 1 "Pico" V 1695 3328 50  0001 L CNN
F 2 "MCU_RaspberryPi_and_Boards:RPi_Pico_SMD_TH" V 1650 2250 50  0001 C CNN
F 3 "" H 1650 2250 50  0001 C CNN
	1    1650 2250
	-1   0    0    1   
$EndComp
Wire Wire Line
	550  3000 800  3000
Connection ~ 900  3100
Wire Wire Line
	800  3000 800  2500
Connection ~ 800  3000
Wire Wire Line
	950  2500 800  2500
Connection ~ 800  2500
Wire Wire Line
	950  2000 800  2000
Wire Wire Line
	800  550  800  1500
Connection ~ 800  2000
Wire Wire Line
	800  2000 800  2500
Wire Wire Line
	950  1500 800  1500
Connection ~ 800  1500
Wire Wire Line
	800  1500 800  2000
Wire Wire Line
	900  650  4950 650 
$Comp
L Device:R R3
U 1 1 612E41A6
P 550 7250
F 0 "R3" V 550 7250 50  0000 C CNN
F 1 "R" V 434 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 480 7250 50  0001 C CNN
F 3 "~" H 550 7250 50  0001 C CNN
	1    550  7250
	1    0    0    -1  
$EndComp
Wire Wire Line
	900  3100 700  3100
Wire Wire Line
	700  3100 700  3300
$Comp
L Device:R R4
U 1 1 612EB595
P 650 7250
F 0 "R4" V 650 7250 50  0000 C CNN
F 1 "R" V 534 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 580 7250 50  0001 C CNN
F 3 "~" H 650 7250 50  0001 C CNN
	1    650  7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R5
U 1 1 612ED660
P 750 7250
F 0 "R5" V 750 7250 50  0000 C CNN
F 1 "R" V 634 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 680 7250 50  0001 C CNN
F 3 "~" H 750 7250 50  0001 C CNN
	1    750  7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R6
U 1 1 612ED666
P 850 7250
F 0 "R6" V 850 7250 50  0000 C CNN
F 1 "R" V 734 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 780 7250 50  0001 C CNN
F 3 "~" H 850 7250 50  0001 C CNN
	1    850  7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R7
U 1 1 612EF989
P 950 7250
F 0 "R7" V 950 7250 50  0000 C CNN
F 1 "R" V 834 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 880 7250 50  0001 C CNN
F 3 "~" H 950 7250 50  0001 C CNN
	1    950  7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R8
U 1 1 612EF98F
P 1050 7250
F 0 "R8" V 1050 7250 50  0000 C CNN
F 1 "R" V 934 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 980 7250 50  0001 C CNN
F 3 "~" H 1050 7250 50  0001 C CNN
	1    1050 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R9
U 1 1 612EF995
P 1150 7250
F 0 "R9" V 1150 7250 50  0000 C CNN
F 1 "R" V 1034 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 1080 7250 50  0001 C CNN
F 3 "~" H 1150 7250 50  0001 C CNN
	1    1150 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R10
U 1 1 612EF99B
P 1250 7250
F 0 "R10" V 1250 7250 50  0000 C CNN
F 1 "R" V 1134 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 1180 7250 50  0001 C CNN
F 3 "~" H 1250 7250 50  0001 C CNN
	1    1250 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R11
U 1 1 612F1FE0
P 1350 7250
F 0 "R11" V 1350 7250 50  0000 C CNN
F 1 "R" V 1234 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 1280 7250 50  0001 C CNN
F 3 "~" H 1350 7250 50  0001 C CNN
	1    1350 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R12
U 1 1 612F1FE6
P 1450 7250
F 0 "R12" V 1450 7250 50  0000 C CNN
F 1 "R" V 1334 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 1380 7250 50  0001 C CNN
F 3 "~" H 1450 7250 50  0001 C CNN
	1    1450 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R13
U 1 1 612F1FEC
P 1550 7250
F 0 "R13" V 1550 7250 50  0000 C CNN
F 1 "R" V 1434 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 1480 7250 50  0001 C CNN
F 3 "~" H 1550 7250 50  0001 C CNN
	1    1550 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R14
U 1 1 612F1FF2
P 1650 7250
F 0 "R14" V 1650 7250 50  0000 C CNN
F 1 "R" V 1534 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 1580 7250 50  0001 C CNN
F 3 "~" H 1650 7250 50  0001 C CNN
	1    1650 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R15
U 1 1 612F1FF8
P 1750 7250
F 0 "R15" V 1750 7250 50  0000 C CNN
F 1 "R" V 1634 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 1680 7250 50  0001 C CNN
F 3 "~" H 1750 7250 50  0001 C CNN
	1    1750 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R16
U 1 1 612F1FFE
P 1850 7250
F 0 "R16" V 1850 7250 50  0000 C CNN
F 1 "R" V 1734 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 1780 7250 50  0001 C CNN
F 3 "~" H 1850 7250 50  0001 C CNN
	1    1850 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R17
U 1 1 612F2004
P 1950 7250
F 0 "R17" V 1950 7250 50  0000 C CNN
F 1 "R" V 1834 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 1880 7250 50  0001 C CNN
F 3 "~" H 1950 7250 50  0001 C CNN
	1    1950 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R18
U 1 1 612F200A
P 2050 7250
F 0 "R18" V 2050 7250 50  0000 C CNN
F 1 "R" V 1934 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 1980 7250 50  0001 C CNN
F 3 "~" H 2050 7250 50  0001 C CNN
	1    2050 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R19
U 1 1 612F6735
P 2150 7250
F 0 "R19" V 2150 7250 50  0000 C CNN
F 1 "R" V 2034 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 2080 7250 50  0001 C CNN
F 3 "~" H 2150 7250 50  0001 C CNN
	1    2150 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R20
U 1 1 612F673B
P 2250 7250
F 0 "R20" V 2250 7250 50  0000 C CNN
F 1 "R" V 2134 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 2180 7250 50  0001 C CNN
F 3 "~" H 2250 7250 50  0001 C CNN
	1    2250 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R21
U 1 1 612F6741
P 2350 7250
F 0 "R21" V 2350 7250 50  0000 C CNN
F 1 "R" V 2234 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 2280 7250 50  0001 C CNN
F 3 "~" H 2350 7250 50  0001 C CNN
	1    2350 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R22
U 1 1 612F6747
P 2450 7250
F 0 "R22" V 2450 7250 50  0000 C CNN
F 1 "R" V 2334 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 2380 7250 50  0001 C CNN
F 3 "~" H 2450 7250 50  0001 C CNN
	1    2450 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R23
U 1 1 612F674D
P 2550 7250
F 0 "R23" V 2550 7250 50  0000 C CNN
F 1 "R" V 2434 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 2480 7250 50  0001 C CNN
F 3 "~" H 2550 7250 50  0001 C CNN
	1    2550 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R24
U 1 1 612F6753
P 2650 7250
F 0 "R24" V 2650 7250 50  0000 C CNN
F 1 "R" V 2534 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 2580 7250 50  0001 C CNN
F 3 "~" H 2650 7250 50  0001 C CNN
	1    2650 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R25
U 1 1 612F6759
P 2750 7250
F 0 "R25" V 2750 7250 50  0000 C CNN
F 1 "R" V 2634 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 2680 7250 50  0001 C CNN
F 3 "~" H 2750 7250 50  0001 C CNN
	1    2750 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R26
U 1 1 612F675F
P 2850 7250
F 0 "R26" V 2850 7250 50  0000 C CNN
F 1 "R" V 2734 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 2780 7250 50  0001 C CNN
F 3 "~" H 2850 7250 50  0001 C CNN
	1    2850 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R27
U 1 1 612F6765
P 3050 7250
F 0 "R27" V 3050 7250 50  0000 C CNN
F 1 "R" V 2934 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 2980 7250 50  0001 C CNN
F 3 "~" H 3050 7250 50  0001 C CNN
	1    3050 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R28
U 1 1 612F676B
P 3150 7250
F 0 "R28" V 3150 7250 50  0000 C CNN
F 1 "R" V 3034 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 3080 7250 50  0001 C CNN
F 3 "~" H 3150 7250 50  0001 C CNN
	1    3150 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R29
U 1 1 612F6771
P 3250 7250
F 0 "R29" V 3250 7250 50  0000 C CNN
F 1 "R" V 3134 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 3180 7250 50  0001 C CNN
F 3 "~" H 3250 7250 50  0001 C CNN
	1    3250 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R30
U 1 1 612F6777
P 3350 7250
F 0 "R30" V 3350 7250 50  0000 C CNN
F 1 "R" V 3234 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 3280 7250 50  0001 C CNN
F 3 "~" H 3350 7250 50  0001 C CNN
	1    3350 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R31
U 1 1 612F677D
P 3450 7250
F 0 "R31" V 3450 7250 50  0000 C CNN
F 1 "R" V 3334 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 3380 7250 50  0001 C CNN
F 3 "~" H 3450 7250 50  0001 C CNN
	1    3450 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R32
U 1 1 612F6783
P 3550 7250
F 0 "R32" V 3550 7250 50  0000 C CNN
F 1 "R" V 3434 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 3480 7250 50  0001 C CNN
F 3 "~" H 3550 7250 50  0001 C CNN
	1    3550 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R33
U 1 1 612F6789
P 3650 7250
F 0 "R33" V 3650 7250 50  0000 C CNN
F 1 "R" V 3534 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 3580 7250 50  0001 C CNN
F 3 "~" H 3650 7250 50  0001 C CNN
	1    3650 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R34
U 1 1 612F678F
P 3750 7250
F 0 "R34" V 3750 7250 50  0000 C CNN
F 1 "R" V 3634 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 3680 7250 50  0001 C CNN
F 3 "~" H 3750 7250 50  0001 C CNN
	1    3750 7250
	1    0    0    -1  
$EndComp
Connection ~ 8250 1950
$Comp
L 74xx:74LS373 U7
U 1 1 613513B1
P 5100 4550
F 0 "U7" V 5100 4600 50  0000 C CNN
F 1 "74LS373" V 5200 4600 50  0000 C CNN
F 2 "Package_SO:SO-20_12.8x7.5mm_P1.27mm" H 5100 4550 50  0001 C CNN
F 3 "http://www.ti.com/lit/gpn/sn74LS373" H 5100 4550 50  0001 C CNN
	1    5100 4550
	0    -1   -1   0   
$EndComp
$Comp
L 74xx:74LS373 U8
U 1 1 6135375F
P 6800 4250
F 0 "U8" V 6800 4300 50  0000 C CNN
F 1 "74LS373" V 6900 4300 50  0000 C CNN
F 2 "Package_SO:SO-20_12.8x7.5mm_P1.27mm" H 6800 4250 50  0001 C CNN
F 3 "http://www.ti.com/lit/gpn/sn74LS373" H 6800 4250 50  0001 C CNN
	1    6800 4250
	0    -1   -1   0   
$EndComp
$Comp
L 74xx:74LS373 U6
U 1 1 61355496
P 1700 4550
F 0 "U6" V 1700 4600 50  0000 C CNN
F 1 "74LS373" V 1800 4600 50  0000 C CNN
F 2 "Package_SO:SO-20_12.8x7.5mm_P1.27mm" H 1700 4550 50  0001 C CNN
F 3 "http://www.ti.com/lit/gpn/sn74LS373" H 1700 4550 50  0001 C CNN
	1    1700 4550
	0    -1   -1   0   
$EndComp
Wire Wire Line
	1900 4050 1900 4000
Wire Wire Line
	1800 4050 1800 4000
Wire Wire Line
	1700 4050 1700 4000
Wire Wire Line
	1600 4050 1600 4000
Wire Wire Line
	1500 4050 1500 4000
Wire Wire Line
	1400 4050 1400 4000
Wire Wire Line
	1300 4050 1300 4000
Wire Wire Line
	1200 4050 1200 4000
Entry Wire Line
	1200 4000 1300 3900
Entry Wire Line
	1300 4000 1400 3900
Entry Wire Line
	1400 4000 1500 3900
Entry Wire Line
	1500 4000 1600 3900
Entry Wire Line
	1600 4000 1700 3900
Entry Wire Line
	1700 4000 1800 3900
Entry Wire Line
	1800 4000 1900 3900
Entry Wire Line
	1900 4000 2000 3900
Wire Bus Line
	2000 3900 2000 3750
Wire Wire Line
	3600 4050 3600 4000
Wire Wire Line
	3500 4050 3500 4000
Wire Wire Line
	3400 4050 3400 4000
Wire Wire Line
	3300 4050 3300 4000
Wire Wire Line
	3200 4050 3200 4000
Wire Wire Line
	3100 4050 3100 4000
Wire Wire Line
	3000 4050 3000 4000
Wire Wire Line
	2900 4050 2900 4000
Entry Wire Line
	2900 4000 3000 3900
Entry Wire Line
	3000 4000 3100 3900
Entry Wire Line
	3100 4000 3200 3900
Entry Wire Line
	3200 4000 3300 3900
Entry Wire Line
	3300 4000 3400 3900
Entry Wire Line
	3400 4000 3500 3900
Entry Wire Line
	3500 4000 3600 3900
Entry Wire Line
	3600 4000 3700 3900
Wire Bus Line
	3700 3900 3700 3750
Wire Wire Line
	5300 4050 5300 4000
Wire Wire Line
	5200 4050 5200 4000
Wire Wire Line
	5100 4050 5100 4000
Wire Wire Line
	5000 4050 5000 4000
Wire Wire Line
	4900 4050 4900 4000
Wire Wire Line
	4800 4050 4800 4000
Wire Wire Line
	4700 4050 4700 4000
Wire Wire Line
	4600 4050 4600 4000
Entry Wire Line
	4600 4000 4700 3900
Entry Wire Line
	4700 4000 4800 3900
Entry Wire Line
	4800 4000 4900 3900
Entry Wire Line
	4900 4000 5000 3900
Entry Wire Line
	5000 4000 5100 3900
Entry Wire Line
	5100 4000 5200 3900
Entry Wire Line
	5200 4000 5300 3900
Entry Wire Line
	5300 4000 5400 3900
Wire Bus Line
	5400 3900 5400 3750
Wire Wire Line
	7000 3750 7000 3700
Wire Wire Line
	6900 3750 6900 3700
Wire Wire Line
	6800 3750 6800 3700
Wire Wire Line
	6700 3750 6700 3700
Wire Wire Line
	6600 3750 6600 3700
Wire Wire Line
	6500 3750 6500 3700
Wire Wire Line
	6400 3750 6400 3700
Wire Wire Line
	6300 3750 6300 3700
Entry Wire Line
	6300 3700 6400 3600
Entry Wire Line
	6400 3700 6500 3600
Entry Wire Line
	6500 3700 6600 3600
Entry Wire Line
	6600 3700 6700 3600
Entry Wire Line
	6700 3700 6800 3600
Entry Wire Line
	6800 3700 6900 3600
Entry Wire Line
	6900 3700 7000 3600
Entry Wire Line
	7000 3700 7100 3600
Connection ~ 3700 3750
Wire Bus Line
	3700 3750 2000 3750
Connection ~ 5400 3750
Wire Bus Line
	5400 3750 3700 3750
Wire Wire Line
	950  1300 700  1300
Wire Wire Line
	950  1400 700  1400
Wire Wire Line
	950  1600 700  1600
Wire Wire Line
	700  1700 950  1700
Wire Wire Line
	700  1800 950  1800
Wire Wire Line
	700  1900 950  1900
Wire Wire Line
	700  2100 950  2100
Wire Wire Line
	700  2300 950  2300
Entry Wire Line
	600  1400 700  1300
Entry Wire Line
	600  1500 700  1400
Entry Wire Line
	600  1700 700  1600
Entry Wire Line
	600  1800 700  1700
Entry Wire Line
	600  1900 700  1800
Entry Wire Line
	600  2000 700  1900
Entry Wire Line
	600  2200 700  2100
Entry Wire Line
	600  2400 700  2300
Wire Bus Line
	600  3750 2000 3750
Connection ~ 2000 3750
Wire Wire Line
	800  3000 800  3550
Wire Wire Line
	5900 4550 5900 3550
Wire Wire Line
	4200 4550 4200 3550
Connection ~ 4200 3550
Wire Wire Line
	4200 3550 4600 3550
Wire Wire Line
	2500 4550 2500 3550
Wire Wire Line
	800  3550 2500 3550
Connection ~ 2500 3550
Wire Wire Line
	2500 3550 4200 3550
Wire Wire Line
	900  3100 900  3450
Wire Wire Line
	6000 3450 6000 4250
Wire Wire Line
	900  4550 900  3450
Connection ~ 900  3450
Wire Wire Line
	900  3450 2600 3450
Wire Wire Line
	2600 3450 2600 4550
Connection ~ 2600 3450
Wire Wire Line
	2600 3450 3850 3450
Wire Wire Line
	4300 4550 4300 3450
Connection ~ 4300 3450
Wire Wire Line
	4300 3450 6000 3450
Wire Wire Line
	1200 5050 550  5050
Wire Wire Line
	550  5050 550  7100
Wire Wire Line
	1300 5050 1300 5100
Wire Wire Line
	1300 5100 650  5100
Wire Wire Line
	650  5100 650  7100
Wire Wire Line
	1400 5050 1400 5150
Wire Wire Line
	1400 5150 750  5150
Wire Wire Line
	750  5150 750  7100
Wire Wire Line
	1500 5050 1500 5200
Wire Wire Line
	1500 5200 850  5200
Wire Wire Line
	850  5200 850  7100
Wire Wire Line
	950  7100 950  5250
Wire Wire Line
	950  5250 1600 5250
Wire Wire Line
	1600 5250 1600 5050
Wire Wire Line
	1700 5050 1700 5300
Wire Wire Line
	1700 5300 1050 5300
Wire Wire Line
	1050 5300 1050 7100
Wire Wire Line
	1800 5050 1800 5350
Wire Wire Line
	1800 5350 1150 5350
Wire Wire Line
	1150 5350 1150 7100
Wire Wire Line
	1900 5050 1900 5400
Wire Wire Line
	1900 5400 1250 5400
Wire Wire Line
	1250 5400 1250 7100
Wire Wire Line
	3000 5050 3000 5500
Wire Wire Line
	1450 5500 1450 7100
Wire Wire Line
	3100 5050 3100 5550
Wire Wire Line
	3100 5550 1550 5550
Wire Wire Line
	1550 5550 1550 7100
Wire Wire Line
	3200 5050 3200 5600
Wire Wire Line
	3200 5600 1650 5600
Wire Wire Line
	1650 5600 1650 7100
Wire Wire Line
	1750 7100 1750 5650
Wire Wire Line
	1750 5650 3300 5650
Wire Wire Line
	3300 5650 3300 5050
Wire Wire Line
	3400 5050 3400 5700
Wire Wire Line
	3400 5700 1850 5700
Wire Wire Line
	1850 5700 1850 7100
Wire Wire Line
	3500 5050 3500 5750
Wire Wire Line
	3500 5750 1950 5750
Wire Wire Line
	1950 5750 1950 7100
Wire Wire Line
	3600 5050 3600 5800
Wire Wire Line
	3600 5800 2050 5800
Wire Wire Line
	2050 5800 2050 7100
Wire Wire Line
	3000 5500 1450 5500
Wire Wire Line
	1350 5450 2900 5450
Wire Wire Line
	2900 5450 2900 5050
Wire Wire Line
	1350 5450 1350 7100
Wire Wire Line
	4700 5050 4700 5900
Wire Wire Line
	2250 5900 2250 7100
Wire Wire Line
	4800 5050 4800 5950
Wire Wire Line
	4800 5950 2350 5950
Wire Wire Line
	2350 5950 2350 7100
Wire Wire Line
	4900 5050 4900 6000
Wire Wire Line
	4900 6000 2450 6000
Wire Wire Line
	2450 6000 2450 7100
Wire Wire Line
	2550 7100 2550 6050
Wire Wire Line
	2550 6050 5000 6050
Wire Wire Line
	5000 6050 5000 5050
Wire Wire Line
	5100 5050 5100 6100
Wire Wire Line
	5100 6100 2650 6100
Wire Wire Line
	2650 6100 2650 7100
Wire Wire Line
	5200 5050 5200 6150
Wire Wire Line
	5200 6150 2750 6150
Wire Wire Line
	2750 6150 2750 7100
Wire Wire Line
	5300 5050 5300 6200
Wire Wire Line
	5300 6200 2850 6200
Wire Wire Line
	2850 6200 2850 7100
Wire Wire Line
	4700 5900 2250 5900
Wire Wire Line
	2150 5850 4600 5850
Wire Wire Line
	4600 5850 4600 5050
Wire Wire Line
	2150 5850 2150 7100
Wire Wire Line
	6400 4750 6400 5350
Wire Wire Line
	3150 6300 3150 7100
Wire Wire Line
	6500 4750 6500 5400
Wire Wire Line
	3250 6350 3250 7100
Wire Wire Line
	6600 4750 6600 5450
Wire Wire Line
	3450 7100 3450 6450
Wire Wire Line
	3450 6450 5800 6450
Wire Wire Line
	6800 4750 6800 5550
Wire Wire Line
	3550 6500 3550 7100
Wire Wire Line
	6900 4750 6900 5600
Wire Wire Line
	3650 6550 3650 7100
Wire Wire Line
	7000 4750 7000 5650
Wire Wire Line
	3750 6600 3750 7100
Wire Wire Line
	3050 6250 5400 6250
Wire Wire Line
	3050 6250 3050 7100
Wire Wire Line
	8950 1850 8900 1850
Wire Wire Line
	7950 900  8950 900 
Wire Wire Line
	8950 900  9050 900 
Connection ~ 8950 900 
Wire Wire Line
	8950 900  8950 1850
Wire Wire Line
	8750 1650 8750 1700
Connection ~ 8750 1650
Connection ~ 10750 800 
Wire Wire Line
	10750 800  10900 800 
Wire Wire Line
	10600 800  10750 800 
Wire Wire Line
	950  2600 450  2600
Wire Wire Line
	450  2600 450  3350
Wire Wire Line
	450  3350 2600 3350
Wire Wire Line
	350  3400 2600 3400
Entry Wire Line
	2600 3400 2700 3300
Entry Wire Line
	2600 3350 2700 3250
Entry Wire Line
	2600 3300 2700 3200
Wire Bus Line
	2700 3200 4650 3200
Wire Bus Line
	4650 3200 4650 2850
Entry Wire Line
	4500 2650 4400 2550
Entry Wire Line
	4500 2750 4400 2650
Entry Wire Line
	4500 2850 4400 2750
Wire Bus Line
	4650 2850 4500 2850
Wire Wire Line
	2550 3100 2550 5250
Wire Wire Line
	2550 5250 2100 5250
Wire Wire Line
	2100 5250 2100 5050
Wire Wire Line
	2550 5250 3800 5250
Wire Wire Line
	3800 5250 3800 5050
Connection ~ 2550 5250
Wire Wire Line
	3800 5250 5500 5250
Wire Wire Line
	5500 5250 5500 5050
Connection ~ 3800 5250
Wire Wire Line
	5500 5250 7200 5250
Wire Wire Line
	7200 5250 7200 4750
Connection ~ 5500 5250
Wire Wire Line
	2200 5050 2200 5150
Wire Wire Line
	2200 5150 2700 5150
Wire Wire Line
	2700 2750 3350 2750
Wire Wire Line
	3350 2650 2750 2650
Wire Wire Line
	2750 2650 2750 5150
Wire Wire Line
	2750 5150 3900 5150
Wire Wire Line
	3900 5150 3900 5050
Wire Wire Line
	3350 2550 2800 2550
Wire Wire Line
	2800 2550 2800 3350
Wire Wire Line
	4400 3350 2800 3350
Wire Wire Line
	3350 2450 2850 2450
Wire Wire Line
	2850 2450 2850 3300
Wire Wire Line
	2850 3300 4450 3300
Wire Wire Line
	4450 3300 4450 5200
Wire Wire Line
	7300 5200 7300 4750
Wire Wire Line
	4400 5150 5600 5150
Wire Wire Line
	4400 3350 4400 5150
Wire Wire Line
	5600 5050 5600 5150
Wire Wire Line
	4450 5200 7300 5200
NoConn ~ 4350 2250
NoConn ~ 4350 2150
NoConn ~ 4350 2050
Text Label 750  2300 0    50   ~ 0
L0
Text Label 750  2100 0    50   ~ 0
L1
Text Label 750  1900 0    50   ~ 0
L2
Text Label 750  1800 0    50   ~ 0
L3
Text Label 750  1700 0    50   ~ 0
L4
Text Label 750  1600 0    50   ~ 0
L5
Text Label 750  1400 0    50   ~ 0
L6
Text Label 750  1300 0    50   ~ 0
L7
Text Label 6300 3750 0    50   ~ 0
L0
Text Label 6400 3750 0    50   ~ 0
L1
Text Label 6500 3750 0    50   ~ 0
L2
Text Label 6600 3750 0    50   ~ 0
L3
Text Label 6700 3750 0    50   ~ 0
L4
Text Label 6800 3750 0    50   ~ 0
L5
Text Label 6900 3750 0    50   ~ 0
L6
Text Label 7000 3750 0    50   ~ 0
L7
Text Label 4600 4050 0    50   ~ 0
L0
Text Label 4700 4050 0    50   ~ 0
L1
Text Label 4800 4050 0    50   ~ 0
L2
Text Label 4900 4050 0    50   ~ 0
L3
Text Label 5000 4050 0    50   ~ 0
L4
Text Label 5100 4050 0    50   ~ 0
L5
Text Label 5200 4050 0    50   ~ 0
L6
Text Label 5300 4050 0    50   ~ 0
L7
Text Label 2900 4050 0    50   ~ 0
L0
Text Label 3000 4050 0    50   ~ 0
L1
Text Label 3100 4050 0    50   ~ 0
L2
Text Label 3200 4050 0    50   ~ 0
L3
Text Label 3300 4050 0    50   ~ 0
L4
Text Label 3400 4050 0    50   ~ 0
L5
Text Label 3500 4050 0    50   ~ 0
L6
Text Label 3600 4050 0    50   ~ 0
L7
Text Label 1200 4050 0    50   ~ 0
L0
Text Label 1300 4050 0    50   ~ 0
L1
Text Label 1400 4050 0    50   ~ 0
L2
Text Label 1500 4050 0    50   ~ 0
L3
Text Label 1600 4050 0    50   ~ 0
L4
Text Label 1700 4050 0    50   ~ 0
L5
Text Label 1800 4050 0    50   ~ 0
L6
Text Label 1900 4050 0    50   ~ 0
L7
Wire Wire Line
	5850 750  6450 750 
Wire Wire Line
	5550 750  4950 750 
Wire Wire Line
	8250 1950 9550 1950
Wire Wire Line
	9550 1450 9550 1950
Wire Wire Line
	350  2450 950  2450
Connection ~ 9550 1450
Wire Wire Line
	350  2450 350  3400
Wire Wire Line
	950  2450 950  2400
Text Label 2500 1300 0    50   ~ 0
CS0
Text Label 750  2450 0    50   ~ 0
CS1
Text Label 750  2600 0    50   ~ 0
CS2
Wire Wire Line
	4400 2550 4350 2550
Wire Wire Line
	4350 2650 4400 2650
Wire Wire Line
	4400 2750 4350 2750
Text Label 4400 2750 0    50   ~ 0
CS0
Text Label 4400 2650 0    50   ~ 0
CS1
Text Label 4400 2550 0    50   ~ 0
CS2
Text Label 3750 3450 0    50   ~ 0
5V
Text Label 3750 3550 0    50   ~ 0
1.6V
Wire Wire Line
	3850 3050 3850 3450
Connection ~ 3850 3450
Wire Wire Line
	3850 3450 4300 3450
Wire Wire Line
	3850 1750 4600 1750
Wire Wire Line
	4600 1750 4600 3050
Connection ~ 4600 3550
Wire Wire Line
	4600 3550 5900 3550
Entry Wire Line
	2550 2400 2450 2500
Entry Wire Line
	2550 2500 2450 2600
Entry Wire Line
	2550 2600 2450 2700
Entry Wire Line
	2550 2700 2450 2800
Wire Wire Line
	2450 2600 2350 2600
Wire Wire Line
	2450 2700 2350 2700
Wire Wire Line
	2350 2800 2450 2800
Entry Wire Line
	2550 1900 2450 2000
Entry Wire Line
	2550 2000 2450 2100
Entry Wire Line
	2550 2100 2450 2200
Entry Wire Line
	2550 2200 2450 2300
Wire Wire Line
	2450 2100 2350 2100
Wire Wire Line
	2450 2200 2350 2200
Wire Wire Line
	2350 2300 2450 2300
Entry Wire Line
	6200 1850 6100 1950
Entry Wire Line
	6100 1850 6000 1950
Entry Wire Line
	6000 1850 5900 1950
Entry Wire Line
	5900 1850 5800 1950
Entry Wire Line
	5800 1850 5700 1950
Entry Wire Line
	5700 1850 5600 1950
Entry Wire Line
	5600 1850 5500 1950
Entry Wire Line
	5500 1850 5400 1950
Wire Wire Line
	6200 1750 6200 1850
Wire Wire Line
	6100 1750 6100 1850
Wire Wire Line
	6000 1750 6000 1850
Wire Wire Line
	5900 1750 5900 1850
Wire Wire Line
	5800 1750 5800 1850
Wire Wire Line
	5700 1750 5700 1850
Wire Wire Line
	5600 1750 5600 1850
Wire Wire Line
	5500 1750 5500 1850
Wire Bus Line
	4950 1950 4950 1900
Wire Bus Line
	4950 1900 2550 1900
Text Label 5500 1850 0    50   ~ 0
DB0
Text Label 5600 1850 0    50   ~ 0
DB1
Text Label 5700 1850 0    50   ~ 0
DB2
Text Label 5800 1850 0    50   ~ 0
DB3
Text Label 5900 1850 0    50   ~ 0
DB4
Text Label 6000 1850 0    50   ~ 0
DB5
Text Label 6100 1850 0    50   ~ 0
DB6
Text Label 6200 1850 0    50   ~ 0
DB7
Text Label 2450 2000 0    50   ~ 0
DB0
Text Label 2450 2100 0    50   ~ 0
DB1
Text Label 2450 2200 0    50   ~ 0
DB2
Text Label 2450 2300 0    50   ~ 0
DB3
Text Label 2450 2500 0    50   ~ 0
DB4
Text Label 2450 2600 0    50   ~ 0
DB5
Text Label 2450 2700 0    50   ~ 0
DB6
Text Label 2450 2800 0    50   ~ 0
DB7
Wire Wire Line
	2450 1800 2450 1850
Wire Wire Line
	2450 1850 5400 1850
Wire Wire Line
	5400 1850 5400 1750
Wire Wire Line
	5300 1750 5300 1800
Wire Wire Line
	4650 1700 2350 1700
Wire Wire Line
	2350 1600 4700 1600
Wire Wire Line
	4700 1750 5200 1750
$Comp
L Device:C C3
U 1 1 61D98CB2
P 4250 3050
F 0 "C3" V 4090 3050 50  0000 C CNN
F 1 "C" V 4089 3050 50  0001 C CNN
F 2 "Capacitor_THT:C_Disc_D3.8mm_W2.6mm_P2.50mm" H 4288 2900 50  0001 C CNN
F 3 "~" H 4250 3050 50  0001 C CNN
	1    4250 3050
	0    -1   -1   0   
$EndComp
Wire Wire Line
	4600 3050 4400 3050
Connection ~ 4600 3050
Wire Wire Line
	4600 3050 4600 3550
Wire Wire Line
	4100 3050 3850 3050
Connection ~ 3850 3050
NoConn ~ 3350 2250
NoConn ~ 3350 2150
NoConn ~ 3350 2050
$Comp
L power:PWR_FLAG #FLG0105
U 1 1 61E8CD4A
P 1650 1000
F 0 "#FLG0105" H 1650 1075 50  0001 C CNN
F 1 "PWR_FLAG" H 1650 1173 50  0000 C CNN
F 2 "" H 1650 1000 50  0001 C CNN
F 3 "~" H 1650 1000 50  0001 C CNN
	1    1650 1000
	1    0    0    -1  
$EndComp
Connection ~ 1650 1000
Text Label 3000 2750 0    50   ~ 0
CE0
Text Label 3000 2650 0    50   ~ 0
CE1
Text Label 3000 2550 0    50   ~ 0
CE2
Text Label 3000 2450 0    50   ~ 0
CE3
Text Label 2500 1400 0    50   ~ 0
LE
Text Label 2400 1600 0    50   ~ 0
RS
Text Label 2400 1700 0    50   ~ 0
RW
Text Label 2400 1800 0    50   ~ 0
E
Text Label 550  7100 0    50   ~ 0
P1
Text Label 650  7100 0    50   ~ 0
P2
Text Label 750  7100 0    50   ~ 0
P3
Text Label 850  7100 0    50   ~ 0
P4
Text Label 950  7100 0    50   ~ 0
P5
Text Label 1050 7100 0    50   ~ 0
P6
Text Label 1150 7100 0    50   ~ 0
P7
Text Label 1250 7100 0    50   ~ 0
P8
Text Label 1350 7100 0    50   ~ 0
P9
Text Label 1450 7100 0    50   ~ 0
P10
Text Label 1550 7100 0    50   ~ 0
P11
Text Label 1650 7100 0    50   ~ 0
P12
Text Label 1750 7100 0    50   ~ 0
P13
Text Label 1850 7100 0    50   ~ 0
P14
Text Label 1950 7100 0    50   ~ 0
P15
Text Label 2050 7100 0    50   ~ 0
P16
Text Label 2150 7100 0    50   ~ 0
P17
Text Label 2250 7100 0    50   ~ 0
P18
Text Label 2350 7100 0    50   ~ 0
P19
Text Label 2450 7100 0    50   ~ 0
P20
Text Label 2550 7100 0    50   ~ 0
P21
Text Label 2650 7100 0    50   ~ 0
P22
Text Label 2750 7100 0    50   ~ 0
P23
Text Label 2850 7100 0    50   ~ 0
P24
Text Label 3050 7100 0    50   ~ 0
P25
Text Label 3150 7100 0    50   ~ 0
P26
Text Label 3250 7100 0    50   ~ 0
P27
Text Label 3350 7100 0    50   ~ 0
P28
Text Label 3450 7100 0    50   ~ 0
P29
Text Label 3550 7100 0    50   ~ 0
P30
Text Label 3650 7100 0    50   ~ 0
P31
Text Label 3750 7100 0    50   ~ 0
P32
$Comp
L 74xx:74LS373 U9
U 1 1 612A7136
P 8500 4250
F 0 "U9" V 8500 4300 50  0000 C CNN
F 1 "74LS373" V 8600 4300 50  0000 C CNN
F 2 "Package_SO:SO-20_12.8x7.5mm_P1.27mm" H 8500 4250 50  0001 C CNN
F 3 "http://www.ti.com/lit/gpn/sn74LS373" H 8500 4250 50  0001 C CNN
	1    8500 4250
	0    -1   -1   0   
$EndComp
Wire Wire Line
	8700 3750 8700 3700
Wire Wire Line
	8600 3750 8600 3700
Wire Wire Line
	8500 3750 8500 3700
Wire Wire Line
	8400 3750 8400 3700
Wire Wire Line
	8300 3750 8300 3700
Wire Wire Line
	8200 3750 8200 3700
Wire Wire Line
	8100 3750 8100 3700
Wire Wire Line
	8000 3750 8000 3700
Entry Wire Line
	8000 3700 8100 3600
Entry Wire Line
	8100 3700 8200 3600
Entry Wire Line
	8200 3700 8300 3600
Entry Wire Line
	8300 3700 8400 3600
Entry Wire Line
	8400 3700 8500 3600
Entry Wire Line
	8500 3700 8600 3600
Entry Wire Line
	8600 3700 8700 3600
Entry Wire Line
	8700 3700 8800 3600
Wire Bus Line
	8800 3600 8800 3350
Wire Bus Line
	8800 3350 7100 3350
Text Label 8000 3750 0    50   ~ 0
L0
Text Label 8100 3750 0    50   ~ 0
L1
Text Label 8200 3750 0    50   ~ 0
L2
Text Label 8300 3750 0    50   ~ 0
L3
Text Label 8400 3750 0    50   ~ 0
L4
Text Label 8500 3750 0    50   ~ 0
L5
Text Label 8600 3750 0    50   ~ 0
L6
Text Label 8700 3750 0    50   ~ 0
L7
$Comp
L Connector:Conn_01x24_Male J2
U 1 1 61334E31
P 1650 7600
F 0 "J2" V 1577 7528 50  0000 C CNN
F 1 "Conn_01x24_Male" V 1486 7528 50  0000 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x24_P2.54mm_Vertical" H 1650 7600 50  0001 C CNN
F 3 "~" H 1650 7600 50  0001 C CNN
	1    1650 7600
	0    -1   -1   0   
$EndComp
$Comp
L Connector:Conn_01x08_Male J4
U 1 1 613A4610
P 3350 7600
F 0 "J4" V 3277 7528 50  0000 C CNN
F 1 "Conn_01x08_Male" V 3186 7528 50  0000 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x08_P2.54mm_Vertical" H 3350 7600 50  0001 C CNN
F 3 "~" H 3350 7600 50  0001 C CNN
	1    3350 7600
	0    -1   -1   0   
$EndComp
Wire Wire Line
	3350 6400 3350 7100
$Comp
L Device:R R35
U 1 1 61582FC1
P 3950 7250
F 0 "R35" V 3950 7250 50  0000 C CNN
F 1 "R" V 3834 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 3880 7250 50  0001 C CNN
F 3 "~" H 3950 7250 50  0001 C CNN
	1    3950 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R36
U 1 1 61582FC7
P 4050 7250
F 0 "R36" V 4050 7250 50  0000 C CNN
F 1 "R" V 3934 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 3980 7250 50  0001 C CNN
F 3 "~" H 4050 7250 50  0001 C CNN
	1    4050 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R37
U 1 1 61582FCD
P 4150 7250
F 0 "R37" V 4150 7250 50  0000 C CNN
F 1 "R" V 4034 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 4080 7250 50  0001 C CNN
F 3 "~" H 4150 7250 50  0001 C CNN
	1    4150 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R38
U 1 1 61582FD3
P 4250 7250
F 0 "R38" V 4250 7250 50  0000 C CNN
F 1 "R" V 4134 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 4180 7250 50  0001 C CNN
F 3 "~" H 4250 7250 50  0001 C CNN
	1    4250 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R39
U 1 1 61582FD9
P 4350 7250
F 0 "R39" V 4350 7250 50  0000 C CNN
F 1 "R" V 4234 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 4280 7250 50  0001 C CNN
F 3 "~" H 4350 7250 50  0001 C CNN
	1    4350 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R40
U 1 1 61582FDF
P 4450 7250
F 0 "R40" V 4450 7250 50  0000 C CNN
F 1 "R" V 4334 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 4380 7250 50  0001 C CNN
F 3 "~" H 4450 7250 50  0001 C CNN
	1    4450 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R41
U 1 1 61582FE5
P 4550 7250
F 0 "R41" V 4550 7250 50  0000 C CNN
F 1 "R" V 4434 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 4480 7250 50  0001 C CNN
F 3 "~" H 4550 7250 50  0001 C CNN
	1    4550 7250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R42
U 1 1 61582FEB
P 4650 7250
F 0 "R42" V 4650 7250 50  0000 C CNN
F 1 "R" V 4534 7250 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 4580 7250 50  0001 C CNN
F 3 "~" H 4650 7250 50  0001 C CNN
	1    4650 7250
	1    0    0    -1  
$EndComp
Wire Wire Line
	4050 6700 4050 7100
Wire Wire Line
	4150 6750 4150 7100
Wire Wire Line
	4350 7100 4350 6850
Wire Wire Line
	4450 6900 4450 7100
Wire Wire Line
	4550 6950 4550 7100
Wire Wire Line
	4650 7000 4650 7100
Wire Wire Line
	3950 6650 3950 7100
Text Label 3950 7100 0    50   ~ 0
P33
Text Label 4050 7100 0    50   ~ 0
P34
Text Label 4150 7100 0    50   ~ 0
P35
Text Label 4250 7100 0    50   ~ 0
P36
Text Label 4350 7100 0    50   ~ 0
P37
Text Label 4450 7100 0    50   ~ 0
P38
Text Label 4550 7100 0    50   ~ 0
P39
Text Label 4650 7100 0    50   ~ 0
P40
$Comp
L Connector:Conn_01x08_Male J5
U 1 1 61583001
P 4250 7600
F 0 "J5" V 4177 7528 50  0000 C CNN
F 1 "Conn_01x08_Male" V 4086 7528 50  0000 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x08_P2.54mm_Vertical" H 4250 7600 50  0001 C CNN
F 3 "~" H 4250 7600 50  0001 C CNN
	1    4250 7600
	0    -1   -1   0   
$EndComp
Wire Wire Line
	4250 6800 4250 7100
Wire Wire Line
	800  550  3200 550 
Wire Wire Line
	6450 750  6450 550 
Connection ~ 6450 750 
Connection ~ 6450 550 
Wire Wire Line
	4950 750  4950 650 
Connection ~ 4950 750 
Connection ~ 4950 650 
Wire Wire Line
	4950 650  7000 650 
Wire Wire Line
	7000 650  7000 2050
Wire Wire Line
	7000 2050 9050 2050
Wire Wire Line
	7100 550  7100 1950
Wire Wire Line
	7100 1950 8250 1950
Wire Wire Line
	6450 550  7100 550 
Wire Wire Line
	5500 950  5500 850 
Wire Wire Line
	5500 850  6900 850 
Wire Wire Line
	6900 850  6900 1650
Wire Wire Line
	6900 1650 8750 1650
Wire Bus Line
	5400 3750 5400 3350
Wire Bus Line
	5400 3350 7100 3350
Wire Bus Line
	7100 3350 7100 3600
Wire Wire Line
	6300 5300 5400 5300
Wire Wire Line
	5400 5300 5400 6250
Wire Wire Line
	6300 5300 6300 4750
Wire Wire Line
	6400 5350 5500 5350
Wire Wire Line
	5500 5350 5500 6300
Wire Wire Line
	5500 6300 3150 6300
Wire Wire Line
	6500 5400 5600 5400
Wire Wire Line
	5600 5400 5600 6350
Wire Wire Line
	5600 6350 3250 6350
Wire Wire Line
	6600 5450 5700 5450
Wire Wire Line
	5700 5450 5700 6400
Wire Wire Line
	5700 6400 3350 6400
Wire Wire Line
	6700 5500 5800 5500
Wire Wire Line
	5800 5500 5800 6450
Wire Wire Line
	6700 5500 6700 4750
Wire Wire Line
	6800 5550 5900 5550
Wire Wire Line
	5900 5550 5900 6500
Wire Wire Line
	5900 6500 3550 6500
Wire Wire Line
	6900 5600 6000 5600
Wire Wire Line
	6000 5600 6000 6550
Wire Wire Line
	6000 6550 3650 6550
Wire Wire Line
	7000 5650 6100 5650
Wire Wire Line
	6100 5650 6100 6600
Wire Wire Line
	6100 6600 3750 6600
Connection ~ 7100 3350
Wire Wire Line
	5900 3550 5900 3250
Wire Wire Line
	5900 3250 7600 3250
Wire Wire Line
	7600 3250 7600 4250
Connection ~ 5900 3550
Wire Wire Line
	7600 3250 9300 3250
Wire Wire Line
	9300 3250 9300 4250
Connection ~ 7600 3250
Wire Wire Line
	7700 4250 7700 3150
Wire Wire Line
	6000 3150 6000 3450
Connection ~ 6000 3450
Wire Wire Line
	8000 4750 8000 5700
Wire Wire Line
	8000 5700 6200 5700
Wire Wire Line
	6200 5700 6200 6650
Wire Wire Line
	6200 6650 3950 6650
Wire Wire Line
	4050 6700 6300 6700
Wire Wire Line
	6300 6700 6300 5750
Wire Wire Line
	6300 5750 8100 5750
Wire Wire Line
	8100 5750 8100 4750
Wire Wire Line
	8200 4750 8200 5800
Wire Wire Line
	8200 5800 6400 5800
Wire Wire Line
	6400 5800 6400 6750
Wire Wire Line
	6400 6750 4150 6750
Wire Wire Line
	4250 6800 6500 6800
Wire Wire Line
	6500 6800 6500 5850
Wire Wire Line
	6500 5850 8300 5850
Wire Wire Line
	8300 5850 8300 4750
Wire Wire Line
	8400 4750 8400 5900
Wire Wire Line
	8400 5900 6600 5900
Wire Wire Line
	6600 5900 6600 6850
Wire Wire Line
	6600 6850 4350 6850
Wire Wire Line
	4450 6900 6700 6900
Wire Wire Line
	6700 6900 6700 5950
Wire Wire Line
	6700 5950 8500 5950
Wire Wire Line
	8500 5950 8500 4750
Wire Wire Line
	8600 4750 8600 6000
Wire Wire Line
	8600 6000 6800 6000
Wire Wire Line
	6800 6000 6800 6950
Wire Wire Line
	6800 6950 4550 6950
Wire Wire Line
	4650 7000 6900 7000
Wire Wire Line
	6900 7000 6900 6050
Wire Wire Line
	6900 6050 8700 6050
Wire Wire Line
	8700 6050 8700 4750
Wire Wire Line
	7200 5250 8900 5250
Wire Wire Line
	8900 5250 8900 4750
Connection ~ 7200 5250
Wire Wire Line
	7700 3150 6000 3150
Wire Wire Line
	3350 2350 2900 2350
Wire Wire Line
	2900 2350 2900 3250
Wire Wire Line
	2900 3250 4750 3250
Wire Wire Line
	4750 3250 4750 3050
Wire Wire Line
	4750 3050 9400 3050
Wire Wire Line
	9400 3050 9400 4850
Wire Wire Line
	9400 4850 9000 4850
Wire Wire Line
	9000 4850 9000 4750
Text Label 3000 2350 0    50   ~ 0
CE4
Wire Wire Line
	2350 1400 2600 1400
Wire Wire Line
	2600 1400 2600 3100
Wire Wire Line
	2600 3100 2550 3100
Wire Wire Line
	2350 1300 2650 1300
Wire Wire Line
	2650 1300 2650 3150
Wire Wire Line
	2650 3150 2600 3150
Wire Wire Line
	2600 3150 2600 3300
Wire Wire Line
	4700 1600 4700 1750
Wire Wire Line
	5300 1800 4650 1800
Wire Wire Line
	4650 1800 4650 1700
Wire Wire Line
	2450 1800 2350 1800
NoConn ~ 2350 2500
Wire Wire Line
	2350 1900 2450 1900
Wire Wire Line
	2450 1900 2450 2000
Wire Wire Line
	2350 2400 2450 2400
Wire Wire Line
	2450 2400 2450 2500
Text Label 5500 850  0    50   ~ 0
V0
$Comp
L Device:LED D4
U 1 1 615F9EE9
P 2900 1200
F 0 "D4" H 2893 945 50  0000 C CNN
F 1 "LED" H 2893 1036 50  0000 C CNN
F 2 "LED_THT:LED_D5.0mm" H 2900 1200 50  0001 C CNN
F 3 "~" H 2900 1200 50  0001 C CNN
	1    2900 1200
	0    1    1    0   
$EndComp
$Comp
L Device:R R43
U 1 1 615F9EEF
P 3050 1350
F 0 "R43" V 3050 1350 50  0000 C CNN
F 1 "R" V 2934 1350 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 2980 1350 50  0001 C CNN
F 3 "~" H 3050 1350 50  0001 C CNN
	1    3050 1350
	0    -1   -1   0   
$EndComp
Wire Wire Line
	2350 2900 3200 2900
Wire Wire Line
	3200 2900 3200 1350
Wire Wire Line
	2350 3100 2400 3100
Wire Wire Line
	2400 3100 2400 2950
Wire Wire Line
	2400 2950 3250 2950
Wire Wire Line
	3250 2950 3250 1650
Wire Wire Line
	2350 3200 2450 3200
Wire Wire Line
	2450 3200 2450 3000
Wire Wire Line
	2450 3000 3300 3000
Wire Wire Line
	3300 3000 3300 1350
$Comp
L Device:LED D6
U 1 1 616F36F7
P 3600 1200
F 0 "D6" H 3593 945 50  0000 C CNN
F 1 "LED" H 3593 1036 50  0000 C CNN
F 2 "LED_THT:LED_D5.0mm" H 3600 1200 50  0001 C CNN
F 3 "~" H 3600 1200 50  0001 C CNN
	1    3600 1200
	0    1    1    0   
$EndComp
$Comp
L Device:R R45
U 1 1 616F36FD
P 3450 1350
F 0 "R45" V 3450 1350 50  0000 C CNN
F 1 "R" V 3334 1350 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 3380 1350 50  0001 C CNN
F 3 "~" H 3450 1350 50  0001 C CNN
	1    3450 1350
	0    -1   -1   0   
$EndComp
$Comp
L Device:LED D5
U 1 1 61748FEB
P 3250 1200
F 0 "D5" H 3243 945 50  0000 C CNN
F 1 "LED" V 3243 1036 50  0000 C CNN
F 2 "LED_THT:LED_D5.0mm" H 3250 1200 50  0001 C CNN
F 3 "~" H 3250 1200 50  0001 C CNN
	1    3250 1200
	0    1    1    0   
$EndComp
$Comp
L Device:R R44
U 1 1 61748FF1
P 3250 1500
F 0 "R44" V 3250 1500 50  0000 C CNN
F 1 "R" V 3134 1500 50  0001 C CNN
F 2 "Resistor_THT:R_Axial_DIN0204_L3.6mm_D1.6mm_P5.08mm_Horizontal" V 3180 1500 50  0001 C CNN
F 3 "~" H 3250 1500 50  0001 C CNN
	1    3250 1500
	1    0    0    -1  
$EndComp
Wire Wire Line
	2900 1050 2900 950 
Wire Wire Line
	2900 950  3200 950 
Wire Wire Line
	3200 950  3200 550 
Connection ~ 3200 550 
Wire Wire Line
	3200 550  3250 550 
Wire Wire Line
	3250 1050 3250 550 
Connection ~ 3250 550 
Wire Wire Line
	3250 550  3300 550 
Connection ~ 3300 550 
Wire Wire Line
	3300 550  6450 550 
Wire Wire Line
	3300 950  3300 550 
Wire Wire Line
	3300 950  3600 950 
Wire Wire Line
	3600 950  3600 1050
Text Label 2700 650  0    50   ~ 0
5V
Wire Wire Line
	900  650  900  3100
Wire Wire Line
	800  3000 950  3000
Wire Wire Line
	950  3000 2350 3000
Wire Bus Line
	4500 2650 4500 2850
Wire Bus Line
	2700 3200 2700 3300
Wire Wire Line
	2700 2750 2700 5150
Wire Bus Line
	1300 3900 2000 3900
Wire Bus Line
	3000 3900 3700 3900
Wire Bus Line
	4700 3900 5400 3900
Wire Bus Line
	6400 3600 7100 3600
Wire Bus Line
	8100 3600 8800 3600
Wire Bus Line
	4950 1950 6100 1950
Wire Bus Line
	2550 1900 2550 2800
Wire Bus Line
	600  1400 600  3750
Connection ~ 950  3000
$EndSCHEMATC
