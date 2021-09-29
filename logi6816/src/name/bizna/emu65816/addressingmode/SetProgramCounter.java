package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import java.util.Arrays;
import java.util.List;

public class SetProgramCounter
    extends CycleOperation
{
  protected List<AddressOffset> addressOffsets;

  public SetProgramCounter(AddressOffset... addressOffsets)
  {
    this.addressOffsets = Arrays.asList(addressOffsets);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setProgramAddress(AddressOffset.getAddress(cpu, addressOffsets));
  }
}

