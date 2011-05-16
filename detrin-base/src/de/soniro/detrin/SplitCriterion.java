/*
 * DeTrIn - Creates and explaines decision trees. 
 * Copyright (C) 2011 Nina Rothenberg
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.soniro.detrin;

import de.soniro.detrin.model.Attribute;

/**
 * Interface for a split-criterion.
 * This interface needs to be implemented if you want to add a new Split-Criterion.
 * 
 * @author Nina Rothenberg
 */
public interface SplitCriterion extends Extensionable, Explainable {

	/**
	 * Calculates and returns the best attribute for a split.
	 * 
	 * @param input - {@link SplitInput}
	 * @return best {@link Attribute} for a split.
	 */
	Attribute<?> getBestAttributeForSplit(SplitInput input);

}
