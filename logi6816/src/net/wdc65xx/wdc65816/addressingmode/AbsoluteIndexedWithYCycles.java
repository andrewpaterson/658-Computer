package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;
import net.wdc65xx.wdc65816.WidthFromRegister;

import static net.wdc65xx.wdc65816.AddressingMode.AbsoluteIndexedWithY;

public class AbsoluteIndexedWithYCycles
    extends InstructionCycles
{
  //7
  public AbsoluteIndexedWithYCycles(Executor<Cpu65816> operation, WidthFromRegister width)
  {
    super(AbsoluteIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(DBR(), AAH(), AAL_YL()), IO(), new NoteFourY(true)),
          new BusCycle(Address(DBR(), AA(), Y()), Read_DataLow(), E8Bit(operation, width), DONE8Bit(width)),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), Read_DataHigh(), E16Bit(operation, width), DONE16Bit(width)));
  }
}

