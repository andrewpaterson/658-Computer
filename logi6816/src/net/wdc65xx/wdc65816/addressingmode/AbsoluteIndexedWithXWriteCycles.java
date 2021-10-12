package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.AbsoluteIndexedWithX;
import static net.wdc65xx.wdc65816.WidthFromRegister.M;

public class AbsoluteIndexedWithXWriteCycles
    extends InstructionCycles
{
  //6a
  public AbsoluteIndexedWithXWriteCycles(Executor<Cpu65816> operation)
  {
    super(AbsoluteIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(DBR(), AAH(), AAL_XL()), IO(), new NoteFourX(false)),
          new BusCycle(Address(DBR(), AA(), X()), E(operation), Write_DataLow(), DONE8Bit(M)),
          new BusCycle(Address(DBR(), AA(), X(), o(1)), Write_DataHigh(), DONE16Bit(M)));
  }
}

