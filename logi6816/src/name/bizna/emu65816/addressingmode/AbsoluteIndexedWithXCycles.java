package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Width;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndexedWithX;

public class AbsoluteIndexedWithXCycles
    extends InstructionCycles
{
  //6a
  public AbsoluteIndexedWithXCycles(Consumer<Cpu65816> operation, Width width)
  {
    super(AbsoluteIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(DBR(), AAH(), AAL_XL()), new NoteFourX(true)),
          new BusCycle(Address(DBR(), AA(), X()), Read_DataLow(), E8Bit(operation), DONE8Bit(width)),
          new BusCycle(Address(DBR(), AA(), X(), o(1)), Read_DataHigh(), E16Bit(operation), DONE16Bit(width)));
  }
}

