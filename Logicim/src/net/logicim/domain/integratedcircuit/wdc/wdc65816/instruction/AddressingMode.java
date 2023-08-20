package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction;

public enum AddressingMode
{
  //PB is 8bit
  //DB is 8bit
  //AA is 16Bit
  //DP is 16Bit
  //SP is 16bit

  //Operand is [Address]

  Absolute,                         //AA = [PB: PC + 2]00 + [PB: PC + 1];                       Address =  DB: AA
  AbsoluteLong,                     //AA = [PB: PC + 2]00 + [PB: PC + 1];  AAB = [PB: PC + 3];  Address = AAB: AA
  AbsoluteIndexedWithX,             //AA = [PB: PC + 2]00 + [PB: PC + 1];                       Address =  DB: AA + X
  AbsoluteLongIndexedWithX,         //AA = [PB: PC + 2]00 + [PB: PC + 1];  AAB = [PB: PC + 3];  Address = AAB: AA + X
  AbsoluteIndexedWithY,             //AA = [PB: PC + 2]00 + [PB: PC + 1];                       Address =  DB: AA + Y

  AbsoluteIndirect,                 //AA = [PB: PC + 2]00 + [PB: PC + 1];   PC =           PB: [00:     AA + 1]00 + [00:     AA]
  AbsoluteIndirectLong,             //AA = [PB: PC + 2]00 + [PB: PC + 1];   PC = [00: AA + 2]: [00:     AA + 1]00 + [00:     AA]
  AbsoluteIndexedIndirectWithX,     //AA = [PB: PC + 2]00 + [PB: PC + 1];   PC =           PB: [PB: AA + X + 1]00 + [PB: AA + X]

  Accumulator,                      //Operand is Accumulator,
  Implied,                          //Operand is implied by Opcode,
  BlockMove,
  StopTheClock,
  OpCode,                           //Operand is (next) Opcode

  Direct,                           //D0 = [PB: PC + 1]                                                 Address =  00: DP + D0
  DirectIndexedWithX,               //D0 = [PB: PC + 1]                                                 Address =  00: DP + D0 + X
  DirectIndexedWithY,               //D0 = [PB: PC + 1]                                                 Address =  00: DP + D0 + Y
  DirectIndirect,                   //D0 = [PB: PC + 1];  AA = [00: DP + D0];                           Address =  DB: AA
  DirectIndirectLong,               //D0 = [PB: PC + 1];  AA = [00: DP + D0]  AAB = [00: DP + D0 + 2];  Address = AAB: AA
  DirectIndexedIndirectWithX,       //D0 = [PB: PC + 1];  AA = [00: DP + D0 + X];                       Address =  DB: AA
  DirectIndirectIndexedWithY,       //D0 = [PB: PC + 1];  AA = [00: DP + D0];                           Address =  DB: AA + Y
  DirectIndirectLongIndexedWithY,   //D0 = [PB: PC + 1];  AA = [00: DP + D0]  AAB = [00: DP + D0 + 2];  Address = AAB: AA + Y

  Immediate,                        //Operand is [PB: PC + 1]00 + [PB: PC + 2]

  StackImplied,                     //Address = SP;   Operand is implied by Opcode
  StackImmediate,                   //Address = SP;   Operand is [PB: PC + 1]00 + [PB: PC + 2]
  StackDirectIndirect,
  StackRelative,                    //S0 = [PB: PC + 1];                       Address = 00: SP + S0
  StackRelativeIndirectIndexedWithY,//S0 = [PB: PC + 1];  AA = [00: SP + S0];  Address = DB: AA + Y

  Relative,                         //R0 = [PB: PC + 1];                    (*)PC = PB: PC + R0
  RelativeLong,                     // R = [PB: PC + 2]00 + [PB: PC + 1];   (*)PC = PB: PC + R

  StackInterruptSoftware,
  StackInterruptHardware,
  WaitForInterrupt
}


