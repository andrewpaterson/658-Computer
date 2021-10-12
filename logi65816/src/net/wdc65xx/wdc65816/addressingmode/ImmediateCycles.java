package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;
import net.wdc65xx.wdc65816.WidthFromRegister;

import static net.wdc65xx.wdc65816.AddressingMode.Immediate;

public class ImmediateCycles
    extends InstructionCycles
{
  //18
  public ImmediateCycles(Executor<Cpu65816> operation, WidthFromRegister width)
  {
    super(Immediate,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), PC_inc(), Read_DataLow(), E8Bit(operation, width), DONE8Bit(width)),
          new BusCycle(Address(PBR(), PC()), PC_inc(), Read_DataHigh(), E16Bit(operation, width), DONE16Bit(width)));
  }
}

