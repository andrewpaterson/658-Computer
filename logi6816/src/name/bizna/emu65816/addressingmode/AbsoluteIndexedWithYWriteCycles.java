package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Width;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndexedWithY;

public class AbsoluteIndexedWithYWriteCycles
    extends InstructionCycles
{
  //7
  public AbsoluteIndexedWithYWriteCycles(Consumer<Cpu65816> operation)
  {
    super(AbsoluteIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(DBR(), AAH(), AAL_YL()), IO(), new NoteFourY(false)),
          new BusCycle(Address(DBR(), AA(), Y()), E(operation), Write_DataLow(), DONE8Bit(Width.A)),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), Write_DataHigh(), DONE16Bit(Width.A)));
  }
}

