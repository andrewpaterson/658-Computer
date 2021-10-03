package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

import java.util.function.Consumer;

public class BlockMoveCycles
    extends InstructionCycles
{
  //9a & 9b
  public BlockMoveCycles(Consumer<Cpu65816> operation)
  {
    super(AddressingMode.BlockMove,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_DBR(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAB(), PC_inc()),
          new BusCycle(Address(AAB(), X()), Read_DataLow()),
          new BusCycle(Address(DBR(), Y()), Write_DataLow(), PC_dec(), E(operation)),
          new BusCycle(Address(DBR(), Y()), IO(), PC_dec()),
          new BusCycle(Address(DBR(), Y()), IO(), PC_dec(), DONE()));
  }
}

