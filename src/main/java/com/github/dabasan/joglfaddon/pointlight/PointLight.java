package com.github.dabasan.joglfaddon.pointlight;

import static com.github.dabasan.basis.coloru8.ColorU8Functions.*;
import static com.github.dabasan.basis.vector.VectorFunctions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.github.dabasan.basis.coloru8.ColorU8;
import com.github.dabasan.basis.vector.Vector;
import com.github.dabasan.joglf.gl.shader.ShaderProgram;

/**
 * Point light
 * 
 * @author Daba
 *
 */
class PointLight {
	private boolean enabled;

	private Vector position;
	private float k0;
	private float k1;
	private float k2;
	private ColorU8 diffuse_color;
	private float diffuse_power;

	private float color_clamp_min;
	private float color_clamp_max;

	private List<ShaderProgram> programs;

	private Function<Boolean, Integer> bti;

	public PointLight() {
		enabled = true;

		position = VGet(0.0f, 50.0f, 0.0f);
		k0 = 0.0f;
		k1 = 0.01f;
		k2 = 0.0001f;
		diffuse_color = GetColorU8(1.0f, 1.0f, 1.0f, 1.0f);
		diffuse_power = 1.0f;

		color_clamp_min = 0.0f;
		color_clamp_max = 1.0f;

		programs = new ArrayList<>();

		bti = b -> {
			if (b == false) {
				return 0;
			} else {
				return 1;
			}
		};
	}

	public boolean IsEnabled() {
		return enabled;
	}
	public Vector GetPosition() {
		return new Vector(position);
	}
	public float GetK0() {
		return k0;
	}
	public float GetK1() {
		return k1;
	}
	public float GetK2() {
		return k2;
	}
	public ColorU8 GetDiffuseColor() {
		return new ColorU8(diffuse_color);
	}
	public float GetDiffusePower() {
		return diffuse_power;
	}
	public float GetColorClampMin() {
		return color_clamp_min;
	}
	public float GetColorClampMax() {
		return color_clamp_max;
	}

	public void Enable(boolean enabled) {
		this.enabled = enabled;
	}
	public void SetPosition(Vector position) {
		this.position = position;
	}
	public void SetK(float k0, float k1, float k2) {
		this.k0 = k0;
		this.k1 = k1;
		this.k2 = k2;
	}

	public void SetDiffuseColor(ColorU8 diffuse_color) {
		this.diffuse_color = diffuse_color;
	}
	public void SetDiffusePower(float diffuse_power) {
		this.diffuse_power = diffuse_power;
	}
	public void SetColorClamp(float min, float max) {
		color_clamp_min = min;
		color_clamp_max = max;
	}

	public void AddProgram(ShaderProgram program) {
		programs.add(program);
	}
	public void RemoveProgram(ShaderProgram program) {
		programs.remove(program);
	}
	public void RemoveAllPrograms() {
		programs.clear();
	}

	public void Update(int index) {
		final String element_name = "lights" + "[" + index + "]";

		for (final var program : programs) {
			program.Enable();
			program.SetUniform(element_name + ".enabled", bti.apply(enabled));
			program.SetUniform(element_name + ".position", position);
			program.SetUniform(element_name + ".k0", k0);
			program.SetUniform(element_name + ".k1", k1);
			program.SetUniform(element_name + ".k2", k2);
			program.SetUniform(element_name + ".diffuse_color", diffuse_color);
			program.SetUniform(element_name + ".diffuse_power", diffuse_power);
			program.SetUniform(element_name + ".color_clamp_min", color_clamp_min);
			program.SetUniform(element_name + ".color_clamp_max", color_clamp_max);
			program.Disable();
		}
	}
}
