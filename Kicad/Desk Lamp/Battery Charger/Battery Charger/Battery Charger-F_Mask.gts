%TF.GenerationSoftware,KiCad,Pcbnew,7.0.5*%
%TF.CreationDate,2024-02-13T09:53:05+02:00*%
%TF.ProjectId,Battery Charger,42617474-6572-4792-9043-686172676572,V0*%
%TF.SameCoordinates,Original*%
%TF.FileFunction,Soldermask,Top*%
%TF.FilePolarity,Negative*%
%FSLAX46Y46*%
G04 Gerber Fmt 4.6, Leading zero omitted, Abs format (unit mm)*
G04 Created by KiCad (PCBNEW 7.0.5) date 2024-02-13 09:53:05*
%MOMM*%
%LPD*%
G01*
G04 APERTURE LIST*
G04 Aperture macros list*
%AMRoundRect*
0 Rectangle with rounded corners*
0 $1 Rounding radius*
0 $2 $3 $4 $5 $6 $7 $8 $9 X,Y pos of 4 corners*
0 Add a 4 corners polygon primitive as box body*
4,1,4,$2,$3,$4,$5,$6,$7,$8,$9,$2,$3,0*
0 Add four circle primitives for the rounded corners*
1,1,$1+$1,$2,$3*
1,1,$1+$1,$4,$5*
1,1,$1+$1,$6,$7*
1,1,$1+$1,$8,$9*
0 Add four rect primitives between the rounded corners*
20,1,$1+$1,$2,$3,$4,$5,0*
20,1,$1+$1,$4,$5,$6,$7,0*
20,1,$1+$1,$6,$7,$8,$9,0*
20,1,$1+$1,$8,$9,$2,$3,0*%
%AMHorizOval*
0 Thick line with rounded ends*
0 $1 width*
0 $2 $3 position (X,Y) of the first rounded end (center of the circle)*
0 $4 $5 position (X,Y) of the second rounded end (center of the circle)*
0 Add line between two ends*
20,1,$1,$2,$3,$4,$5,0*
0 Add two circle primitives to create the rounded ends*
1,1,$1,$2,$3*
1,1,$1,$4,$5*%
%AMRotRect*
0 Rectangle, with rotation*
0 The origin of the aperture is its center*
0 $1 length*
0 $2 width*
0 $3 Rotation angle, in degrees counterclockwise*
0 Add horizontal line*
21,1,$1,$2,0,0,$3*%
G04 Aperture macros list end*
%ADD10R,1.700000X1.700000*%
%ADD11O,1.700000X1.700000*%
%ADD12RoundRect,0.250000X-0.475000X0.250000X-0.475000X-0.250000X0.475000X-0.250000X0.475000X0.250000X0*%
%ADD13RotRect,1.700000X1.700000X225.000000*%
%ADD14HorizOval,1.700000X0.000000X0.000000X0.000000X0.000000X0*%
%ADD15RotRect,1.700000X1.700000X315.000000*%
%ADD16HorizOval,1.700000X0.000000X0.000000X0.000000X0.000000X0*%
%ADD17C,3.200000*%
%ADD18R,1.400000X0.600000*%
G04 APERTURE END LIST*
D10*
%TO.C,J_{Battery+}1*%
X6985000Y-5289000D03*
D11*
X6985000Y-2749000D03*
%TD*%
D10*
%TO.C,J_{VUSB+}1*%
X-7110000Y-5289000D03*
D11*
X-7110000Y-2749000D03*
%TD*%
D12*
%TO.C,C1*%
X-4062000Y-5542999D03*
X-4062000Y-7442999D03*
%TD*%
D13*
%TO.C,J_{VOUT}1*%
X-3733973Y4109000D03*
D14*
X-5530024Y5905051D03*
%TD*%
D15*
%TO.C,J_{Switch}1*%
X5346562Y5981561D03*
D16*
X3550511Y4185510D03*
%TD*%
D17*
%TO.C,H1*%
X0Y0D03*
%TD*%
D18*
%TO.C,IC2*%
X869000Y-8316000D03*
X869000Y-7366000D03*
X869000Y-6416000D03*
X-1631000Y-6416000D03*
X-1631000Y-8316000D03*
%TD*%
D12*
%TO.C,C2*%
X3939000Y-5543000D03*
X3939000Y-7443000D03*
%TD*%
M02*
