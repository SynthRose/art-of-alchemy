require 'csv'
require 'json'
require 'fileutils'

FileUtils.mkdir("calcination") unless File.directory?("calcination")
FileUtils.mkdir("dissolution") unless File.directory?("dissolution")
FileUtils.mkdir("synthesis") unless File.directory?("synthesis")

sheet = CSV.open("sheet.csv", headers:true)
sheet.each do |row|
  id = row[1]
  filename = "#{row[1].gsub(":", "_")}.json"
  next if id.nil?

  unless id.include?("!")
    id = "minecraft:" + id
  end

  type = if row[2].nil? then "item" else "tag" end
  restricted = !row[3].nil?

  materia = "artofalchemy:materia_" + row[4].downcase unless row[4].nil?
  materia_amt = row[5].to_r

  essentia = {}
  for i in 6..18
    e = sheet.headers[i].downcase.prepend("artofalchemy:")
    essentia[e] = row[i].to_i unless row[i].nil?
  end

  unless materia.nil?
    recipe = {}
    recipe["type"] = "artofalchemy:calcination"
    recipe["ingredient"] = { type => id }
    recipe["result"] = { "item" => materia, "count" => materia_amt.numerator }
    recipe["container"] = { "item" => "minecraft:bucket" } if id.include?("bucket")
    recipe["factor"] = 1.0/materia_amt.denominator unless materia_amt.denominator == 1
    file = File.new("calcination/#{filename}", mode = 'w')
    file.write(JSON.generate(recipe))
  end

  unless essentia.empty?
    recipe = {}
    recipe["type"] = "artofalchemy:dissolution"
    recipe["ingredient"] = { type => id }
    recipe["result"] = essentia
    recipe["container"] = { "item" => "minecraft:bucket" } if id.include?("bucket")
    file = File.new("dissolution/#{filename}", mode = 'w')
    file.write(JSON.generate(recipe))
  end

  unless restricted || materia.nil?
    recipe = {}
    recipe["type"] = "artofalchemy:synthesis"
    recipe["target"] = { type => id }
    recipe["materia"] = { "item" => materia }
    recipe["essentia"] = essentia
    recipe["cost"] = materia_amt.numerator
    recipe["container"] = { "item" => "minecraft:bucket" } if id.include?("bucket")
    file = File.new("synthesis/#{filename}", mode = 'w')
    file.write(JSON.generate(recipe))
  end
end
