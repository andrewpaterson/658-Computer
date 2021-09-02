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
L MPU_65xx:W65C816SxQ MPU1
U 1 1 610688E3
P 1600 3750
F 0 "MPU1" H 1600 5481 50  0000 C CNN
F 1 "W65C816SxQ" H 1600 5390 50  0000 C CIB
F 2 "Package_QFP:PQFP-44_10x10mm_P0.8mm" H 1600 5750 50  0001 C CNN
F 3 "http://www.westerndesigncenter.com/wdc/documentation/w65c816s.pdf" H 1600 5650 50  0001 C CNN
	1    1600 3750
	1    0    0    -1  
$EndComp
$Comp
L 74xx:74LS148 U3
U 1 1 6106D306
P 4150 6200
F 0 "U3" V 4196 5456 50  0000 R CNN
F 1 "74LS148" V 4105 5456 50  0000 R CNN
F 2 "Package_SO:SOIC-16_4.55x10.3mm_P1.27mm" H 4150 6200 50  0001 C CNN
F 3 "http://www.ti.com/lit/gpn/sn74LS148" H 4150 6200 50  0001 C CNN
	1    4150 6200
	0    -1   -1   0   
$EndComp
$Comp
L Connector_Generic:Conn_01x08 J1
U 1 1 613183D7
P 4050 6950
F 0 "J1" V 3968 6462 50  0000 R CNN
F 1 "Conn_01x08" V 4013 6462 50  0001 R CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x08_P2.54mm_Vertical" H 4050 6950 50  0001 C CNN
F 3 "~" H 4050 6950 50  0001 C CNN
	1    4050 6950
	0    -1   1    0   
$EndComp
Wire Wire Line
	4450 6700 4450 6750
Wire Wire Line
	4350 6700 4350 6750
Wire Wire Line
	4250 6700 4250 6750
Wire Wire Line
	4150 6700 4150 6750
Wire Wire Line
	4050 6700 4050 6750
Wire Wire Line
	3950 6700 3950 6750
Wire Wire Line
	3850 6700 3850 6750
Wire Wire Line
	3750 6700 3750 6750
Wire Wire Line
	4550 6700 4550 7000
Text Label 4550 7000 0    50   ~ 0
GND
Wire Wire Line
	4850 6200 4850 6700
Wire Wire Line
	4850 6700 4550 6700
Connection ~ 4550 6700
Text Label 3450 6200 0    50   ~ 0
VCC
$Comp
L 65C816_Unit-rescue:74LVC161PW,112-SamacSys_Parts IC4
U 1 1 612FBFEA
P 10100 5450
F 0 "IC4" H 10600 5715 50  0000 C CNN
F 1 "74LVC161PW,112" H 10600 5624 50  0000 C CNN
F 2 "SamacSys:SOP65P640X110-16N" H 10950 5550 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC161.pdf" H 10950 5450 50  0001 L CNN
F 4 "74LVC161 - Presettable synchronous 4-bit binary counter; asynchronous reset@en-us" H 10950 5350 50  0001 L CNN "Description"
F 5 "1.1" H 10950 5250 50  0001 L CNN "Height"
F 6 "771-LVC161PW112" H 10950 5150 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC161PW112?qs=me8TqzrmIYXBQ64YL7POQw%3D%3D" H 10950 5050 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 10950 4950 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC161PW,112" H 10950 4850 50  0001 L CNN "Manufacturer_Part_Number"
	1    10100 5450
	1    0    0    -1  
$EndComp
$Comp
L 65C816_Unit-rescue:74ALVC164245DGG_11-SamacSys_Parts IC2
U 1 1 612FDA93
P 5850 700
F 0 "IC2" V 6554 828 50  0000 L CNN
F 1 "74ALVC164245DGG_11" V 6645 828 50  0000 L CNN
F 2 "SamacSys:SOP50P810X120-48N" H 7200 800 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74ALVC164245.pdf" H 7200 700 50  0001 L CNN
F 4 "74ALVC164245 - 16-bit dual supply translating transceiver; 3-state@en-us" H 7200 600 50  0001 L CNN "Description"
F 5 "1.2" H 7200 500 50  0001 L CNN "Height"
F 6 "771-74ALVC164245" H 7200 400 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74ALVC164245DGG11?qs=odJ8N4Y39o4j0ykFPcnqaQ%3D%3D" H 7200 300 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 7200 200 50  0001 L CNN "Manufacturer_Name"
F 9 "74ALVC164245DGG:11" H 7200 100 50  0001 L CNN "Manufacturer_Part_Number"
	1    5850 700 
	0    1    1    0   
$EndComp
$Comp
L 65C816_Unit-rescue:74LVC1G80GW,125-SamacSys_Parts IC1
U 1 1 61301B94
P 5000 2800
F 0 "IC1" H 5500 3065 50  0000 C CNN
F 1 "74LVC1G80GW,125" H 5500 2974 50  0000 C CNN
F 2 "SamacSys:SOT65P212X110-5N" H 5850 2900 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC1G80.pdf" H 5850 2800 50  0001 L CNN
F 4 "74LVC1G80 - Single D-type flip-flop; positive-edge trigger@en-us" H 5850 2700 50  0001 L CNN "Description"
F 5 "1.1" H 5850 2600 50  0001 L CNN "Height"
F 6 "771-LVC1G80GW125" H 5850 2500 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC1G80GW125/?qs=me8TqzrmIYUQqgMUiTiToA%3D%3D" H 5850 2400 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 5850 2300 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC1G80GW,125" H 5850 2200 50  0001 L CNN "Manufacturer_Part_Number"
	1    5000 2800
	1    0    0    -1  
$EndComp
$Comp
L 65C816_Unit-rescue:74LVC373ADB,112-SamacSys_Parts IC6
U 1 1 61304659
P 11950 2900
F 0 "IC6" H 12450 3165 50  0000 C CNN
F 1 "74LVC373ADB,112" H 12450 3074 50  0000 C CNN
F 2 "SamacSys:SOP65P775X200-20N" H 12800 3000 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC373A.pdf" H 12800 2900 50  0001 L CNN
F 4 "74LVC373A - Octal D-type transparent latch with 5 V tolerant inputs/outputs; 3-State@en-us" H 12800 2800 50  0001 L CNN "Description"
F 5 "2" H 12800 2700 50  0001 L CNN "Height"
F 6 "" H 12800 2600 50  0001 L CNN "Mouser Part Number"
F 7 "" H 12800 2500 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 12800 2400 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC373ADB,112" H 12800 2300 50  0001 L CNN "Manufacturer_Part_Number"
	1    11950 2900
	0    -1   -1   0   
$EndComp
Text Label 2200 2550 0    50   ~ 0
A0
Text Label 2200 2650 0    50   ~ 0
A1
Text Label 2200 2750 0    50   ~ 0
A2
Text Label 2200 2850 0    50   ~ 0
A3
Text Label 2200 2950 0    50   ~ 0
A4
Text Label 2200 3050 0    50   ~ 0
A5
Text Label 2200 3150 0    50   ~ 0
A6
Text Label 2200 3250 0    50   ~ 0
A7
Text Label 2200 3350 0    50   ~ 0
A8
Text Label 2200 3450 0    50   ~ 0
A9
Text Label 2200 3550 0    50   ~ 0
A10
Text Label 2200 3650 0    50   ~ 0
A11
Text Label 2200 3750 0    50   ~ 0
A12
Text Label 2200 3850 0    50   ~ 0
A13
Text Label 2200 3950 0    50   ~ 0
A14
Text Label 2200 4050 0    50   ~ 0
A15
Wire Wire Line
	2200 4250 2400 4250
Wire Wire Line
	2200 4350 2400 4350
Wire Wire Line
	2200 4450 2400 4450
Wire Wire Line
	2200 4550 2400 4550
Wire Wire Line
	2200 4650 2400 4650
Wire Wire Line
	2200 4750 2400 4750
Wire Wire Line
	2200 4850 2400 4850
Wire Wire Line
	2200 4950 2400 4950
Text Label 2200 4250 0    50   ~ 0
A16
Text Label 2200 4350 0    50   ~ 0
A17
Text Label 2200 4450 0    50   ~ 0
A18
Text Label 2200 4550 0    50   ~ 0
A19
Text Label 2200 4650 0    50   ~ 0
A20
Text Label 2200 4750 0    50   ~ 0
A21
Text Label 2200 4850 0    50   ~ 0
A22
Text Label 2200 4950 0    50   ~ 0
A23
$Comp
L 65C816_Unit-rescue:74LVC16373ADGG,118-SamacSys_Parts IC5
U 1 1 613008AE
P 11250 750
F 0 "IC5" V 11804 878 50  0000 L CNN
F 1 "74LVC16373ADGG,118" V 11895 878 50  0000 L CNN
F 2 "SamacSys:SOP50P810X120-48N" H 12300 850 50  0001 L CNN
F 3 "https://assets.nexperia.com/documents/data-sheet/74LVC_LVCH16373A.pdf" H 12300 750 50  0001 L CNN
F 4 "74LVC16373A; 74LVCH16373A - 16-bit D-type transparent latch with 5 V tolerant inputs/outputs; 3-state@en-us" H 12300 650 50  0001 L CNN "Description"
F 5 "1.2" H 12300 550 50  0001 L CNN "Height"
F 6 "771-74LVC16373ADGG1" H 12300 450 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/Nexperia/74LVC16373ADGG118?qs=qAI9lz2tHCORIVfLxKahAg%3D%3D" H 12300 350 50  0001 L CNN "Mouser Price/Stock"
F 8 "Nexperia" H 12300 250 50  0001 L CNN "Manufacturer_Name"
F 9 "74LVC16373ADGG,118" H 12300 150 50  0001 L CNN "Manufacturer_Part_Number"
	1    11250 750 
	0    1    1    0   
$EndComp
$Comp
L 65C816_Unit-rescue:IS63WV1288DBLL-10TLI-SamacSys_Parts IC3
U 1 1 613098FD
P 6150 3100
F 0 "IC3" H 6750 3365 50  0000 C CNN
F 1 "IS63WV1288DBLL-10TLI" H 6750 3274 50  0000 C CNN
F 2 "SamacSys:SOIC127P1176X120-32N" H 7200 3200 50  0001 L CNN
F 3 "https://www.issi.com/WW/pdf/63-64WV1288DALL-DBLL.pdf" H 7200 3100 50  0001 L CNN
F 4 "SRAM 1M (128Kx8) 10ns Async SRAM 3.3v" H 7200 3000 50  0001 L CNN "Description"
F 5 "1.2" H 7200 2900 50  0001 L CNN "Height"
F 6 "870-63WV1288DB10TLI" H 7200 2800 50  0001 L CNN "Mouser Part Number"
F 7 "https://www.mouser.co.uk/ProductDetail/ISSI/IS63WV1288DBLL-10TLI/?qs=NDi87N0IjkGAihl3CXK75w%3D%3D" H 7200 2700 50  0001 L CNN "Mouser Price/Stock"
F 8 "Integrated Silicon Solution Inc." H 7200 2600 50  0001 L CNN "Manufacturer_Name"
F 9 "IS63WV1288DBLL-10TLI" H 7200 2500 50  0001 L CNN "Manufacturer_Part_Number"
	1    6150 3100
	1    0    0    -1  
$EndComp
$EndSCHEMATC
