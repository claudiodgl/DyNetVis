/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2014-2019 Bruno Augusto Nassif Travencolo.
 * All Rights Reserved.
 *
 * This file is part of DyNetVis Project (DyNetVis).
 *
 * How to cite this software:
 *  
@inproceedings{Linhares:2017:DSV:3019612.3019686,
 author = {Linhares, Claudio D. G. and Traven\c{c}olo, Bruno A. N. and Paiva, Jose Gustavo S. and Rocha, Luis E. C.},
 title = {DyNetVis: A System for Visualization of Dynamic Networks},
 booktitle = {Proceedings of the Symposium on Applied Computing},
 series = {SAC '17},
 year = {2017},
 isbn = {978-1-4503-4486-9},
 location = {Marrakech, Morocco},
 pages = {187--194},
 numpages = {8},
 url = {http://doi.acm.org/10.1145/3019612.3019686},
 doi = {10.1145/3019612.3019686},
 acmid = {3019686},
 publisher = {ACM},
 address = {New York, NY, USA},
 keywords = {complex networks, dynamic graph visualization, dynamic networks, recurrent neighbors, temporal activity map},
} 
 *  
 * DyNetVis is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * DyNetVis is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * This code was developed by members of Lab of Complex Network Visualization at 
 * Federal University of Uberlândia, Brazil - (https://sites.google.com/view/dynetvis/team?authuser=0). 
 * The initial developer of the original code is Claudio D. G. Linhares <claudiodgl@gmail.com>.
 *
 * Contributor(s): Jean R. Ponciano -- jeanrobertop@gmail.com, Luis E. C. Rocha -- luis.rocha@ugent.be, 
 * José Gustavo S. Paiva -- gustavo@ufu.br, Bruno A. N. Travençolo -- travencolo@gmail.com
 *
 * You should have received a copy of the GNU General Public License along 
 * with DyNetVis. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizationbasics.color;

public class ColorScaleFactory {

    public enum ColorScaleType {

        HEATED_OBJECTS("Heated objects scale"),
        GRAY_SCALE("Gray scale"),
        BLUE_SCALE("Blue scale"),
        LINEAR_GRAY_SCALE("Linear gray scale"),
        LOCS_SCALE("Linear Optminal Color Scale (LOCS)"),
        RAINBOW_SCALE("Rainbow scale"),
        PSEUDO_RAINBOW_SCALE("Pseudo-rainbow scale"),
        DYNAMIC_SCALE("Dynamic scale"),
        CATEGORY_SCALE("Category scale"),
        BLUE_TO_YELLOW_SCALE("Blue to yellow scale"),
        BLUE_TO_CYAN("Blue to cyan scale"),
        GREEN_TO_WHITE_SCALE("Green to white scale");

        private ColorScaleType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        private final String name;
    }

    public static ColorScale getInstance(ColorScaleType type) {

        if (type == ColorScaleType.HEATED_OBJECTS) {
            return new HeatedObjectScale();
        } else if (type == ColorScaleType.GRAY_SCALE) {
            return new GrayScale();
        } else if (type == ColorScaleType.LINEAR_GRAY_SCALE) {
            return new LinearGrayScale();
        } else if (type == ColorScaleType.LOCS_SCALE) {
            return new LocsScale();
        } else if (type == ColorScaleType.RAINBOW_SCALE) {
            return new RainbowScale();
        } else if (type == ColorScaleType.PSEUDO_RAINBOW_SCALE) {
            return new GreenToRed();
        } else if (type == ColorScaleType.CATEGORY_SCALE) {
            return new CategoryScale();
        } else if (type == ColorScaleType.BLUE_TO_YELLOW_SCALE) {
            return new BlueToYellowScale();
        } else if (type == ColorScaleType.BLUE_TO_CYAN) {
            return new BlueToCyanScale();
        } else if (type == ColorScaleType.GREEN_TO_WHITE_SCALE) {
            return new GreenToWhiteScale();
        } else if (type == ColorScaleType.DYNAMIC_SCALE) {
            return new DynamicScale();
        }

        return null;
    }
}
