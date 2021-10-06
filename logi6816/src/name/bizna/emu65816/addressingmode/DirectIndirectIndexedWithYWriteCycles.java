package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Width;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.DirectIndirectIndexedWithY;

public class DirectIndirectIndexedWithYWriteCycles
    extends InstructionCycles
{
  //13
  public DirectIndirectIndexedWithYWriteCycles(Consumer<Cpu65816> operation)
  {
    super(DirectIndirectIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(DBR(), AAH(), AAL_YL()), IO(), new NoteFourY(false)),
          new BusCycle(Address(DBR(), AA(), Y()), E(operation), Write_DataLow(), DONE8Bit(Width.A)),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), Write_DataHigh(), DONE16Bit(Width.A)));
  }
}

