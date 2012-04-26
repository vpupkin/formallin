/*******************************************************************************
 * Copyright (c) 2009, Adobe Systems Incorporated
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * ·        Redistributions of source code must retain the above copyright 
 *          notice, this list of conditions and the following disclaimer. 
 *
 * ·        Redistributions in binary form must reproduce the above copyright 
 *		   notice, this list of conditions and the following disclaimer in the
 *		   documentation and/or other materials provided with the distribution. 
 *
 * ·        Neither the name of Adobe Systems Incorporated nor the names of its 
 *		   contributors may be used to endorse or promote products derived from
 *		   this software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR 
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/

package com.adobe.dp.fb2;

import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class FB2DateInfo {

	private String humanReadable;

	private String machineReadable;

	private boolean yearOnly;

	public String getMachineReadable() {
		return machineReadable;
	}

	public void setMachineReadable(String date) {
		this.machineReadable = date;
	}

	public String getHumanReadable() {
		return humanReadable;
	}

	public void setHumanReadable(String humanReadable) {
		this.humanReadable = humanReadable;
	}

	public Date getDate() {
		try {
			if (machineReadable != null) {
				yearOnly = machineReadable.matches("\\d\\d\\d\\d");
				if (yearOnly) {
					GregorianCalendar cal = new GregorianCalendar(Integer.parseInt(machineReadable), 0, 1, 12, 0);
					return cal.getTime();
				}				
				return DateFormat.getDateInstance().parse(machineReadable);
			}
		} catch (Exception e) {
		}
		try {
			if (humanReadable != null) {
				yearOnly = humanReadable.matches("\\d\\d\\d\\d");
				if (yearOnly) {
					GregorianCalendar cal = new GregorianCalendar(Integer.parseInt(humanReadable), 0, 1, 12, 0);
					return cal.getTime();
				}				
				return DateFormat.getDateInstance().parse(humanReadable);
			}
		} catch (Exception e) {
		}
		return null;
	}

	public boolean isYearOnly() {
		return yearOnly;
	}
}
