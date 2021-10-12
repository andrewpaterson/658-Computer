package net.wdc65xx.wdc65816.addressingmode;

import static net.wdc65xx.wdc65816.AddressingMode.AbsoluteIndirect;

public class AbsoluteIndirectJMPCycles
    extends InstructionCycles
{
  //3b
  public AbsoluteIndirectJMPCycles()
  {
    super(AbsoluteIndirect,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(AA()), Read_NewPCL()),
          new BusCycle(Address(AA(), o(1)), Read_NewPCH(), PC_e(PBR(), New_PC()), DONE()));
  }
}

